package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.exception.UnauthorizedException;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.VerificationTokenRepository;
import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.VerificationToken;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationService authenticationService;

    public String folderPath = "C:\\Users\\XXX\\Pictures\\"; // dummy folderpath


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



    public Account deleteAccount(String username) {

        if(!authenticationService.checkUsernameSession(username)){
            throw new UnauthorizedException("No permission");
        }

        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        if(account == null){
            throw new UsernameNotFoundException("User does not exist");
        }

        account.setDeleted(true);
        account.setDeletedAt(LocalDateTime.now());
        accountRepository.save(account);
        return account;
    }

    // Upload, Get, Delete Profile Picture

    public String uploadProfileImage(String username, MultipartFile file)throws IOException {

        if(!authenticationService.checkUsernameSession(username)){
            throw new UnauthorizedException("No permission");
        }

        String filePath = folderPath + file.getOriginalFilename();
        AccountDetailDTO newAccountDetails = getProfile(username);
        newAccountDetails.setImageUrl(filePath);
        updateProfile(username, newAccountDetails);
        return "File uploaded successfully: " + filePath;
    }


    public byte[] getProfileImage(String username) throws IOException{
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        String filePath = account.getImageUrl();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public String deleteProfileImage(String username){

        if(!authenticationService.checkUsernameSession(username)){
            throw new UnauthorizedException("No permission");
        }

        AccountDetailDTO accountDetailDTO = getProfile(username);
        accountDetailDTO.setImageUrl(null);
        updateProfile(username, accountDetailDTO);

        if(accountRepository.findByUsernameAndIsDeletedFalse(username).getImageUrl() == null) {
            return "Profile picture of " + username + " deleted successfully.";
        }

        return null;
    }

    // To retrieve current user profile data from db

    public AccountDetailDTO getProfile(String username){
        Account currentProfile = accountRepository.findByUsernameAndIsDeletedFalse(username);
        AccountDetailDTO accountDetailDTO = AccountDetailDTO.builder().
                displayName(currentProfile.getDisplayName()).
                email(currentProfile.getEmail()).
                username(username).
                imageUrl(currentProfile.getImageUrl()).
                created(currentProfile.getCreated()).
//                loginAttempt(currentProfile.getLoginAttempt()).
                setting(currentProfile.getSetting()).
                build();
        return accountDetailDTO;
    }

    // Saves edited profile details

    public void updateProfile(String username, AccountDetailDTO accountDetailDTO) {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.setDisplayName(accountDetailDTO.getDisplayName());
        account.setEmail(accountDetailDTO.getEmail());
        account.setUsername(accountDetailDTO.getUsername());
        account.setImageUrl(accountDetailDTO.getImageUrl());
        accountRepository.save(account);
    }





}
