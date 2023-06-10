package com.scrapheap.account.controller;

import com.scrapheap.account.dto.AccountCreateDTO;
import com.scrapheap.account.dto.AccountDetailDTO;
import com.scrapheap.account.repository.AccountRepository;
import com.scrapheap.account.service.AccountService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PutMapping("/{username}/imageUpload")
    public ResponseEntity<?> uploadProfileImage(@PathVariable("username") String username,
                                                @RequestParam("image")MultipartFile file) throws IOException {
        String uploadImageResponse = accountService.uploadProfileImage(username, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImageResponse);
    }

    @GetMapping("/{username}/imageRetrieve")
    public ResponseEntity<?> retrieveProfileImage(@PathVariable String username) throws IOException{
        byte[] imageData = accountService.getProfileImage(username);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @DeleteMapping("/{username}/imageDelete")
    public ResponseEntity<?> deleteProfileImage(@PathVariable String username){
        String response = accountService.deleteProfileImage(username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable String username){
        String response = accountService.deleteAccount(username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);

    }
}
