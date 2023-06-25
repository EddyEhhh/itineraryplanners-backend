package com.scrapheap.itineraryplanner.service;


import com.scrapheap.itineraryplanner.dto.AccountCreateDTO;
import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.AuthenticationResponseDTO;
import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(AccountCreateDTO accountDTO, String applicationUrl){
        String defaultRole = "USER";
        LocalDateTime timeNow = LocalDateTime.now();
        Account account = Account.builder().
                displayName(accountDTO.getDisplayName()).
                email(accountDTO.getEmail()).
                username(accountDTO.getUsername()).
                password(passwordEncoder.encode(accountDTO.getPassword())).
                role(defaultRole).
                build();

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(account,
                applicationUrl));

        accountRepository.save(account);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(accountDTO.getUsername());
        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(AccountCredentialDTO accountDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountDTO.getUsername(),
                        accountDTO.getPassword()
                )
        );
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(accountDTO.getUsername());
        if(userDetails == null){
            throw new UsernameNotFoundException("No user found");
        }

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

}
