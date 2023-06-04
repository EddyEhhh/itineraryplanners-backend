package com.scrapheap.account.service;

import com.scrapheap.account.dto.AccountCreateDTO;
import com.scrapheap.account.dto.AccountDetailDTO;
import com.scrapheap.account.model.Account;
import com.scrapheap.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    public boolean createAccount(AccountCreateDTO accountDTO){
        LocalDateTime timeNow = LocalDateTime.now();
        Account account = Account.builder().
        displayName(accountDTO.getDisplayName()).
        email(accountDTO.getEmail()).
        username(accountDTO.getUsername()).
        password(accountDTO.getPassword()).
        build();
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

}
