package com.scrapheap.account.controller;

import com.scrapheap.account.dto.AccountCreateDTO;
import com.scrapheap.account.dto.AccountDetailDTO;
import com.scrapheap.account.repository.AccountRepository;
import com.scrapheap.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDetailDTO> getAcccounts(){
        return accountService.getAccounts();
    }

    @PostMapping
    public void register(@RequestBody AccountCreateDTO accountDTO) {
        accountService.createAccount(accountDTO);
    }




}
