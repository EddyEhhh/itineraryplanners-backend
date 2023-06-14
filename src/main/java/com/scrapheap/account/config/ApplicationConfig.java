package com.scrapheap.account.config;

import com.scrapheap.account.model.*;
import com.scrapheap.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository) {
        return args -> {

            List<Account> accountToCreate = new ArrayList<Account>();

            accountToCreate.add(
                    generateAccount(
                            "Leo",
                            "Leo@example.com",
                            "leo_x",
                            "Password1!"));
            accountToCreate.add(
                    generateAccount(
                            "Jia Wei",
                            "jw@example.com",
                            "jaq_jw",
                            "Password1!"));
            accountToCreate.add(
                    generateAccount(
                            "Ash",
                            "ashley@example.com",
                            "ashley",
                            "Password1!"));
            accountToCreate.add(
                    generateAccount(
                            "Fang",
                            "Fangwww@example.com",
                            "fangwww",
                            "Password1!"));
            accountToCreate.add(
                    generateAccount(
                            "Clar",
                            "Clarissa@example.com",
                            "clarissa",
                            "Password1!"));
            accountToCreate.add(
                    generateAccount(
                            "John Doe",
                            "John@example.com",
                            "johndoe",
                            "Password1!"));

            accountRepository.saveAll(accountToCreate);
        };
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



