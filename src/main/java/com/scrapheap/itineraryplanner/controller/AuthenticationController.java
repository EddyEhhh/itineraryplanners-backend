package com.scrapheap.itineraryplanner.controller;


import com.scrapheap.itineraryplanner.dto.AccountCreateDTO;
import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
import com.scrapheap.itineraryplanner.dto.AuthenticationResponseDTO;
import com.scrapheap.itineraryplanner.service.AccountService;
import com.scrapheap.itineraryplanner.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;

    private final AuthenticationService authenticationService;

//    @PostMapping("/register")
//    public void register(@RequestBody AccountCreateDTO accountDTO, final HttpServletRequest request) {
//        String applicationUrl = getApplicationUrl(request);
//        accountService.createAccount(accountDTO, applicationUrl);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AccountCreateDTO accountDTO, final HttpServletRequest request) {
        String applicationUrl = getApplicationUrl(request);
        String authenticationResponse = authenticationService.register(accountDTO, applicationUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authenticationResponse)
                .body("Success");
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid AccountCredentialDTO accountDTO, final HttpServletRequest request, HttpServletResponse response) {
        String applicationUrl = getApplicationUrl(request);
//        AuthenticationResponseDTO authenticationResponse = authenticationService.authenticate(accountDTO);
//        response.setHeader("Authorization", authenticationResponse.getToken());

        String authenticationResponse = authenticationService.authenticate(accountDTO);
//        Cookie cookieJwt = new Cookie("token", authenticationResponse.getToken());
//        cookieJwt.setMaxAge(300);
//        cookieJwt.setHttpOnly(true);
//        cookieJwt.setSecure(true);
//        response.addCookie(cookieJwt);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authenticationResponse)
                .body("Success");
    }

//    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody @Valid AccountCredentialDTO accountDTO, final HttpServletRequest request, HttpServletResponse response) {
//        String applicationUrl = getApplicationUrl(request);
//        AuthenticationResponseDTO authenticationResponse = authenticationService.authenticate(accountDTO);
//        Cookie cookie = new Cookie("token", authenticationResponse.getToken());
//        cookie.setMaxAge(300);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        response.addCookie(cookie);
//        return ResponseEntity.ok(authenticationService.authenticate(accountDTO));
//    }

    @GetMapping
    public ResponseEntity<AccountDetailDTO> getSessionAccount(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountDetailDTO accountDetailDTO = authenticationService.getAccountDetail(username);
//        AccountDetailDTO accountDetailDTO = authenticationService.getCurrentSession();
        return ResponseEntity.ok(accountDetailDTO);
    }



    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/verify")
    public void verifyRegistration(@RequestParam("token") String token){
        authenticationService.validateVerficiationToken(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/resendVerify")
    public void resendVerifyRegistration(@RequestParam("token") String oldToken,
                                         HttpServletRequest request){
        String applicationUrl = getApplicationUrl(request);
        authenticationService.generateNewVerificationToken(oldToken, applicationUrl);
    }

    private String getApplicationUrl(HttpServletRequest request){
        return String.format("http://%s:%s%s",
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath());
    }

}
