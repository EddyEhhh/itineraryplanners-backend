package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.ChangePasswordDTO;
import com.scrapheap.itineraryplanner.dto.ForgotPasswordDTO;
import com.scrapheap.itineraryplanner.dto.ForgotPasswordEmailDTO;
import com.scrapheap.itineraryplanner.exception.InvalidPasswordException;
import com.scrapheap.itineraryplanner.exception.UnauthorizedException;
import com.scrapheap.itineraryplanner.model.ForgotPassword;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.ForgotPasswordRepository;
import com.scrapheap.itineraryplanner.repository.VerificationTokenRepository;
import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.VerificationToken;
import com.scrapheap.itineraryplanner.s3.S3Buckets;
import com.scrapheap.itineraryplanner.s3.S3Service;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Buckets s3Buckets;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AccountDetailDTO> getAccounts(){
        List<Account> accounts = accountRepository.findAll();

        List<AccountDetailDTO> accountDTOList = new ArrayList<AccountDetailDTO>();
        for(Account eachAccount : accounts){
            AccountDetailDTO accountDTO = AccountDetailDTO.builder().
                    displayName(eachAccount.getDisplayName()).
                    email(eachAccount.getEmail()).
                    username(eachAccount.getUsername()).
                    imageId(eachAccount.getImageId()).
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

        String profileImageId = UUID.randomUUID().toString();
        //puts into aws
        try {
            s3Service.putObject(
                    s3Buckets.getAccount(),
                    "profile-images/%s/%s".formatted(username, profileImageId),
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.setImageId(profileImageId);
        accountRepository.save(account);

        return "File uploaded Successfully";
    }

    public String getProfileImage(String username) throws IOException{
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        if(account.getImageId().isBlank()){
            throw new RuntimeException("account profile image not found");
        }
        var profileImageId = account.getImageId();
        return "https://ip-account.s3.ap-southeast-1.amazonaws.com/profile-images/" + username + "/" + profileImageId;
        // return s3Service.getObject(s3Buckets.getAccount(), "profile-images/%s/%s".formatted(username, profileImageId));
    }


    public String deleteProfileImage(String username){
        //TODO: implement this
        if(!authenticationService.checkUsernameSession(username)){
            throw new UnauthorizedException("No permission");
        }

        AccountDetailDTO accountDetailDTO = getProfile(username);
        accountDetailDTO.setImageId(null);
        updateProfile(username, accountDetailDTO);

        if(accountRepository.findByUsernameAndIsDeletedFalse(username).getImageId() == null) {
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
                imageId(currentProfile.getImageId()).
                created(currentProfile.getCreated()).
//                loginAttempt(currentProfile.getLoginAttempt()).
                setting(currentProfile.getSetting()).
                build();
        return accountDetailDTO;
    }

    // Saves edited profile details
    //TODO: double check imageId part
    public String updateProfile(String username, AccountDetailDTO accountDetailDTO) {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.setDisplayName(accountDetailDTO.getDisplayName());
        account.setEmail(accountDetailDTO.getEmail());
        accountRepository.save(account);
        return "new account details saved";
    }

    // forgot password

    public boolean validPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        return true;
    }

    public boolean forgotPasswordEmail(ForgotPasswordEmailDTO forgotPasswordEmailDTO) {
        String forgotPasswordToken = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
        ForgotPassword forgotPassword = ForgotPassword.builder().
                token(forgotPasswordToken).
                expirationTime(expirationTime).
                build();
        Account account = accountRepository.findByEmailAndIsDeletedFalse(forgotPasswordEmailDTO.getEmail());
        account.setForgotPassword(forgotPassword);
        accountRepository.save(account);
        return true;
    }

    public boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        ForgotPassword forgotPassword = forgotPasswordRepository.findForgotPasswordByToken(forgotPasswordDTO.getToken());
        Account account = accountRepository.findByForgotPassword(forgotPassword);
        if (validPassword(forgotPasswordDTO.getNewPassword())) {
            account.setPassword(forgotPasswordDTO.getNewPassword());
            accountRepository.save(account);
            return true;
        } else {
            throw new InvalidPasswordException("Please enter a password that has at least 8 characters");
        }
    }

    // change password

    public boolean changePassword(ChangePasswordDTO changePasswordDTO, String username) {
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        String password = account.getPassword();

        //TODO: find some way to check oldpassword equals to new password (encoded)

        if (changePasswordDTO.getOldPassword().equals(password)) {
            if(validPassword(changePasswordDTO.getNewPassword())) {
                account.setPassword(changePasswordDTO.getNewPassword());
                System.out.println("password successfully changed");
                return true;
            } else {
                throw new InvalidPasswordException("Please enter a password that has at least 8 characters");
            }
        } else {
            System.out.println("Invalid password entered");
            return false;
        }
    }

}
