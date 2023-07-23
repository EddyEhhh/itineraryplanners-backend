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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private VerificationTokenRepository verificationTokenRepository;

    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    public AccountService(AccountRepository accountRepository, VerificationTokenRepository verificationTokenRepository,
            PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String folderPath = "C:\\Users\\XXX\\Pictures\\"; // dummy folderpath

    public boolean createAccount(AccountCreateDTO accountDTO, String applicationUrl) {
        String defaultRole = "USER";
        LocalDateTime timeNow = LocalDateTime.now();
        Account account = Account.builder().displayName(accountDTO.getDisplayName()).email(accountDTO.getEmail())
                .username(accountDTO.getUsername()).password(passwordEncoder.encode(accountDTO.getPassword()))
                .role(defaultRole).build();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        accountRepository.save(account);

        return true;
    }

    public List<AccountDetailDTO> getAccounts() {
        List<Account> accounts = accountRepository.findAll();

        List<AccountDetailDTO> accountDTOList = new ArrayList<AccountDetailDTO>();
        for (Account eachAccount : accounts) {
            AccountDetailDTO accountDTO = AccountDetailDTO.builder().displayName(eachAccount.getDisplayName())
                    .email(eachAccount.getEmail()).username(eachAccount.getUsername())
                    .imageUrl(eachAccount.getImageUrl()).created(eachAccount.getCreated()).build();
            accountDTOList.add(accountDTO);
        }
        return accountDTOList;
    }

    public void createVerificationToken(Account account, String token) {
        LocalDateTime expirationTimestamp = LocalDateTimeUtil.calculateExpirationTimestamp(10);
        VerificationToken verificationToken = VerificationToken.builder().token(token)
                .expirationTimestamp(expirationTimestamp).account(account).build();

        verificationTokenRepository.save(verificationToken);

    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        Account account = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();

        if (verificationToken.getExpirationTimestamp().isBefore(LocalDateTime.now())) {
            // verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        account.setVerified(true);
        verificationTokenRepository.delete(verificationToken);
        accountRepository.save(account);
        return "valid";
    }

    public VerificationToken generateNewVerificationToken(String oldToken, String applicationUrl) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        Account account = verificationToken.getAccount();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        return verificationToken;
    }

    public String deleteAccount(String username) {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.setDeleted(true);
        account.setDeletedAt(LocalDateTime.now());
        accountRepository.save(account);
        if (accountRepository.findByUsername(username).getDeletedAt() != null) {
            return "Account " + username + " was deleted successfully.";
        }
        return null;
    }

    // Upload, Get, Delete Profile Picture

    public String uploadProfileImage(String username, MultipartFile file) throws IOException {
        String filePath = folderPath + file.getOriginalFilename();
        AccountDetailDTO newAccountDetails = retrieveCurrentProfileData(username);
        newAccountDetails.setImageUrl(filePath);
        saveNewProfileDetails(username, newAccountDetails);
        return "File uploaded successfully: " + filePath;
    }

    public byte[] getProfileImage(String username) throws IOException {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        String filePath = account.getImageUrl();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public String deleteProfileImage(String username) {
        AccountDetailDTO accountDetailDTO = retrieveCurrentProfileData(username);
        accountDetailDTO.setImageUrl(null);
        saveNewProfileDetails(username, accountDetailDTO);
        if (accountRepository.findByUsernameAndIsDeletedFalse(username).getImageUrl() == null) {
            return "Profile picture of " + username + " deleted successfully.";
        }
        return null;
    }

    // To retrieve current user profile data from db

    public AccountDetailDTO retrieveCurrentProfileData(String username) {
        Account currentProfile = accountRepository.findByUsernameAndIsDeletedFalse(username);
        AccountDetailDTO accountDetailDTO = AccountDetailDTO.builder().displayName(currentProfile.getDisplayName())
                .email(currentProfile.getEmail()).username(username).imageUrl(currentProfile.getImageUrl())
                .created(currentProfile.getCreated()).loginAttempt(currentProfile.getLoginAttempt())
                .setting(currentProfile.getSetting()).build();
        return accountDetailDTO;
    }

    // Saves edited profile details

    public void saveNewProfileDetails(String username, AccountDetailDTO accountDetailDTO) {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.setDisplayName(accountDetailDTO.getDisplayName());
        account.setEmail(accountDetailDTO.getEmail());
        account.setUsername(accountDetailDTO.getUsername());
        account.setImageUrl(accountDetailDTO.getImageUrl());
        accountRepository.save(account);
    }

}
