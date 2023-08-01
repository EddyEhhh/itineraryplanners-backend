package com.scrapheap.itineraryplanner.controller;

import com.scrapheap.itineraryplanner.dto.*;
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
import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PutMapping("/{username}")
    public ResponseEntity<AccountUpdateDTO> updateAccount(@PathVariable("username")String username,
                                                          @RequestBody AccountUpdateDTO accountUpdateDTO) {
        AccountUpdateDTO updateAccountResponse = accountService.updateProfile(username, accountUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateAccountResponse);
    }

    @PostMapping(value = "/{username}/imageUpload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadProfileImage(@PathVariable("username") String username,
                                                @RequestParam("image")MultipartFile file) throws IOException {
        String uploadImageResponse = accountService.uploadProfileImage(username, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImageResponse);
    }

    @GetMapping(value = "/{username}/imageRetrieve"
    )
    public String retrieveProfileImage(@PathVariable("username") String username) throws IOException{
        return accountService.getProfileImage(username);
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

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{username}/updatePassword")
    public ResponseEntity<?> updatePassword(@PathVariable("username") String username,
                                            @RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean response = accountService.changePassword(changePasswordDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
