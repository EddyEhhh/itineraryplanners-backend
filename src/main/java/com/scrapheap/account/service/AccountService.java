package com.scrapheap.account.service;

import com.scrapheap.account.dto.AccountCreateDTO;
import com.scrapheap.account.dto.AccountDetailDTO;
import com.scrapheap.account.event.RegistrationCompleteEvent;
import com.scrapheap.account.model.Account;
import com.scrapheap.account.model.VerificationToken;
import com.scrapheap.account.repository.AccountRepository;
import com.scrapheap.account.repository.VerificationTokenRepository;
import com.scrapheap.account.util.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public boolean createAccount(AccountCreateDTO accountDTO, String applicationUrl){
        String defaultRole = "USER";
        LocalDateTime timeNow = LocalDateTime.now();
        Account account = Account.builder().
                displayName(accountDTO.getDisplayName()).
                email(accountDTO.getEmail()).
                username(accountDTO.getUsername()).
                password(passwordEncoder.encode(accountDTO.getPassword())).
                role(defaultRole).
                build();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));



        accountRepository.save(account);

        return true;
    }

    public List<AccountDetailDTO> getAccounts(){
        List<Account> accounts = accountRepository.findAll();

        List<AccountDetailDTO> accountDTOList = new ArrayList<AccountDetailDTO>();
        for(Account eachAccount : accounts){
            AccountDetailDTO accountDTO = AccountDetailDTO.builder().
                    displayName(eachAccount.getDisplayName()).
                    email(eachAccount.getEmail()).
                    username(eachAccount.getUsername()).
                    imageUrl(eachAccount.getImageUrl()).
                    created(eachAccount.getCreated()).
                    build();
            accountDTOList.add(accountDTO);
        }
        return accountDTOList;
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

    public String validateVerficiationToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            return "invalid";
        }

        Account account = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();

        if(verificationToken.getExpirationTimestamp().isBefore(LocalDateTime.now())){
//            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }


        account.setVerified(true);
        verificationTokenRepository.delete(verificationToken);
        accountRepository.save(account);
        return "valid";
    }

    public VerificationToken generateNewVerificationToken(String oldToken, String applicationUrl){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        Account account = verificationToken.getAccount();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        return verificationToken;
    }



}
