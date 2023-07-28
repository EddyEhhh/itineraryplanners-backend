package com.scrapheap.itineraryplanner.controller;

import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.dto.ChangePasswordDTO;
import com.scrapheap.itineraryplanner.dto.ForgotPasswordDTO;
import com.scrapheap.itineraryplanner.dto.ForgotPasswordEmailDTO;
import com.scrapheap.itineraryplanner.model.Account;
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

//    @GetMapping
//    public List<AccountDetailDTO> getAcccounts(){
//        log.info(accountService.getAccounts().toString());
//        return accountService.getAccounts();
//    }


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

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{username}")
    public void deleteAccount(@PathVariable String username){
        Account response = accountService.deleteAccount(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> forgotPasswordEmail(@PathVariable ForgotPasswordEmailDTO forgotPasswordEmailDTO) {
        boolean response = accountService.forgotPasswordEmail(forgotPasswordEmailDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@PathVariable ForgotPasswordDTO forgotPasswordDTO) {
        boolean response = accountService.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forgotPassword/token")
    public ResponseEntity<?> changePassword(@PathVariable ChangePasswordDTO changePasswordDTO, String username) {
        boolean response = accountService.changePassword(changePasswordDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
