package com.scrapheap.itineraryplanner.controller;

import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.service.AccountService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<AccountDetailDTO> getAcccounts(){
        log.info(accountService.getAccounts().toString());
        return accountService.getAccounts();
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
