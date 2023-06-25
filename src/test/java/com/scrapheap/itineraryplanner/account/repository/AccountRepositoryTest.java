package com.scrapheap.itineraryplanner.account.repository;

import com.scrapheap.itineraryplanner.account.model.Account;
import com.scrapheap.itineraryplanner.account.model.LoginAttempt;
import com.scrapheap.itineraryplanner.account.model.Setting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;


@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void saveAccount(){
        Account account = Account.builder()
                .displayName("Test User")
                .email("User@bmail.com")
                .username("Username")
                .password("Password123")
                .build();

        accountRepository.save(account);

    }

    @Test
    public void printUserByEmail(){
        saveAccount();
        Account accounts = accountRepository.findByEmailAndIsDeletedFalse("User@bmail.com");
        System.out.println(accounts);
    }

    @Test
    public void printAccountJson(){
        Account account = generateAccount(
                        "John Doe",
                        "John@example.com",
                        "johndoe",
                        "Password1!");

        System.out.println(ResponseEntity.ok(account));

    }


    public Account generateAccount(String displayName, String email, String username,  String password){
        LocalDateTime currentTime = LocalDateTime.now();

        LoginAttempt loginAttempt = LoginAttempt.builder().
                numberOfAttempts(0).
                lastAttemptTimestamp(currentTime).
                build();

        Setting setting = Setting.builder().
                language("en").
                theme("light").
                build();

//            ForgotPassword forgotPassword1 = ForgotPassword.builder().
////                    token().
////                    timestamp().
//                    build();
//
//            Session session1 = Session.builder().
////                    token().
////                    timestamp().
//                    build();

        Account account = Account.builder().
                displayName(displayName).
                email(email).
                username(username).
                password(password).
                created(currentTime).
                loginAttempt(loginAttempt).
                setting(setting).
//                    forgotPassword(forgotPassword1).
//                    session(session1).
        build();

        return account;
    }


}