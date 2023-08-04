package com.scrapheap.itineraryplanner.service;


import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.exception.AlreadyExistsException;
import com.scrapheap.itineraryplanner.exception.InvalidAuthenticationException;
import com.scrapheap.itineraryplanner.exception.InvalidPasswordException;
import com.scrapheap.itineraryplanner.exception.UnauthorizedException;
import com.scrapheap.itineraryplanner.model.LoginAttempt;
import com.scrapheap.itineraryplanner.model.Setting;
import com.scrapheap.itineraryplanner.model.VerificationToken;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.dto.AccountCreateDTO;
import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.AuthenticationResponseDTO;
import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.repository.VerificationTokenRepository;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(AccountCreateDTO accountDTO, String applicationUrl){

//        log.info("TEST: " + accountRepository.findByUsernameAndIsDeletedFalse(accountDTO.getUsername()).toString());

        if (accountRepository.findByUsernameAndIsDeletedFalse(accountDTO.getUsername()) != null){
            log.info("USERNAME EXIST----------------------------------------------------------------");
            throw new AlreadyExistsException("authenticate.error.internal.username.exist", "username");
        }

        if(accountRepository.findByEmailAndIsDeletedFalse(accountDTO.getEmail()) != null){
            log.info("EMAIL EXIST----------------------------------------------------------------");
            throw new AlreadyExistsException("authenticate.error.internal.email.exist", "email");
        }

        String defaultRole = "USER";
        LocalDateTime timeNow = LocalDateTime.now();

        Setting setting = Setting.builder().
                language("en").
                theme("light").
                build();

        LoginAttempt loginAttempt = LoginAttempt.builder().
                numberOfAttempts(0).
                lastAttemptTimestamp(timeNow).
                build();

        Account account = Account.builder().
                displayName(accountDTO.getDisplayName()).
                email(accountDTO.getEmail()).
                username(accountDTO.getUsername()).
                password(passwordEncoder.encode(accountDTO.getPassword())).
                role(defaultRole).
                created(timeNow).
                setting(setting).
                loginAttempt(loginAttempt).
                build();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        accountRepository.save(account);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(accountDTO.getUsername());
        var jwtToken = jwtService.generateToken(userDetails);
        return jwtToken;
    }

    public String authenticate(AccountCredentialDTO accountDTO){

        UserDetails userDetails;
        try {
            userDetails = this.userDetailsService.loadUserByUsername(accountDTO.getUsername());
        }catch (UsernameNotFoundException usernameNotFoundException){
            throw new InvalidAuthenticationException("authentication.error.generic");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            accountDTO.getUsername(),
                            accountDTO.getPassword()
                    )
            );
        }catch (AuthenticationException authenticationException){

//            if(!passwordEncoder.matches(accountDTO.getPassword(), userDetails.getPassword())){
//                log.info("---------- wrong password");
////                throw new InvalidPasswordException("authentication.error.invalid.password");
                throw new InvalidAuthenticationException("authentication.error.generic");

        }

        var jwtToken = jwtService.generateToken(userDetails);
        return jwtToken;
    }


    public void createVerificationToken(Account account, String token){
        LocalDateTime expirationTimestamp = LocalDateTimeUtil.
                calculateExpirationTimestamp(10);
        VerificationToken verificationToken = VerificationToken.builder().
                token(token).
                expirationTimestamp(expirationTimestamp).
                account(account).
                build();

        verificationTokenRepository.save(verificationToken);

    }

    public boolean validateVerficiationToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            throw new IllegalArgumentException("Token not found");
        }

        Account account = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();

        if(verificationToken.getExpirationTimestamp().isBefore(LocalDateTime.now())){
//            verificationTokenRepository.delete(verificationToken);
            throw new IllegalArgumentException("Token expired");
        }


        account.setVerified(true);
        verificationTokenRepository.delete(verificationToken);
        accountRepository.save(account);
        return true;
    }

    public VerificationToken generateNewVerificationToken(String oldToken, String applicationUrl){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        Account account = verificationToken.getAccount();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        return verificationToken;
    }

    public AccountDetailDTO getAccountDetail(String username){
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        AccountDetailDTO accountDetailDTO = AccountDetailDTO.builder()
                .displayName(account.getDisplayName())
                .email(account.getEmail())
                .username(account.getUsername())
                .imageId(account.getImageId())
                .created(account.getCreated())
//                .loginAttempt(account.getLoginAttempt())
//                .setting(account.getSetting())
                .build();
        return accountDetailDTO;
    }

    public boolean checkUsernameSession(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals(username)){
            return true;
        }
        return false;
    }

    public AccountDetailDTO getCurrentSession(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        AccountDetailDTO accountDetailDTO = AccountDetailDTO.builder()
                .username(account.getUsername())
                .displayName(account.getDisplayName())
                .build();
        return accountDetailDTO;
    }

//    public AccountDetailDTO getCurrentAccountDetail() {
//
//        log.info(username + " is the current user");
//        return getAccountDetail("jaq_jw");
//    }
}
