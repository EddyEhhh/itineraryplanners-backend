package com.scrapheap.itineraryplanner.controller;


import com.scrapheap.itineraryplanner.dto.AccountCreateDTO;
import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.AuthenticationResponseDTO;
import com.scrapheap.itineraryplanner.service.AccountService;
import com.scrapheap.itineraryplanner.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
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
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody AccountCreateDTO accountDTO, final HttpServletRequest request) {
        String applicationUrl = getApplicationUrl(request);
        return ResponseEntity.ok(authenticationService.register(accountDTO, applicationUrl));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody AccountCredentialDTO accountDTO, final HttpServletRequest request) {
        String applicationUrl = getApplicationUrl(request);
        return ResponseEntity.ok(authenticationService.authenticate(accountDTO));
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
