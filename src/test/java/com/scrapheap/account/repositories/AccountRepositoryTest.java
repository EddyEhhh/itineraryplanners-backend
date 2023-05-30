package com.scrapheap.account.repositories;

import com.scrapheap.account.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        List<Account> accounts = accountRepository.findByEmailAndIsDeletedFalse("User@bmail.com");
        System.out.println(accounts);
    }


}