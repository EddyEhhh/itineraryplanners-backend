package com.scrapheap.itineraryplanner.service;


import com.scrapheap.itineraryplanner.exception.AlreadyExistsException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public AuthenticationResponseDTO register(AccountCreateDTO accountDTO, String applicationUrl){

        if (accountRepository.findByUsernameAndIsDeletedFalse(accountDTO.getUsername()) != null){
            throw new AlreadyExistsException("Username already exists");
        }

        if(accountRepository.findByEmailAndIsDeletedFalse(accountDTO.getEmail()) != null){
            throw new AlreadyExistsException("Email already in use");
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
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(AccountCredentialDTO accountDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountDTO.getUsername(),
                        accountDTO.getPassword()
                )
        );
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(accountDTO.getUsername());
        if(userDetails == null){
            throw new UsernameNotFoundException("No user found");
        }

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
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

    public boolean checkUsernameSession(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals(username)){
            return true;
        }
        return false;
    }

}
