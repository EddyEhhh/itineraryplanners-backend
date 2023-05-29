package com.scrapheap.account.config;

import com.scrapheap.account.models.Account;
import com.scrapheap.account.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository) {
//        String encodedPassword = bCryptPasswordEncoder().encode("123");
        LocalDateTime localDateTime = LocalDateTime.now();
        return args -> {

            Account account1 = Account.builder().
                    displayName("Leo").
                    email("Leo@example.com").
                    username("Leo").
                    password("Password123!").
                    created(localDateTime).
                    build();


            accountRepository.saveAll(
                    List.of(account1)
            );
        };
    }
}