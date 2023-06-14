package com.scrapheap.account.controller;

import com.scrapheap.account.dto.AccountCreateDTO;
import com.scrapheap.account.dto.AccountDetailDTO;
import com.scrapheap.account.model.Account;
import com.scrapheap.account.model.VerificationToken;
import com.scrapheap.account.repository.AccountRepository;
import com.scrapheap.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
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
    public void register(@RequestBody AccountCreateDTO accountDTO, final HttpServletRequest request) {
        String applicationUrl = getApplicationUrl(request);
        accountService.createAccount(accountDTO, applicationUrl);
    }

    @GetMapping("/verify")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = accountService.validateVerficiationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "success";
        }
        return "invalid token";
    }

    @GetMapping("/resendVerify")
    public void resendVerifyRegistration(@RequestParam("token") String oldToken,
                                           HttpServletRequest request){
        String applicationUrl = getApplicationUrl(request);
        accountService.generateNewVerificationToken(oldToken, applicationUrl);
    }

    private String getApplicationUrl(HttpServletRequest request){
        return String.format("http://%s:%s%s",
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath());
    }




}
