package com.scrapheap.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AccountDetailDTO {

    private String displayName;

    private String email;

    private String username;

    private String imageUrl = null;

    private LocalDateTime created;

//    private LoginAttempt loginAttempt;
//
//
//    private Setting setting;


}



