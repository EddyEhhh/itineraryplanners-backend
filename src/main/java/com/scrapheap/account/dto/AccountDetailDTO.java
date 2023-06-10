package com.scrapheap.account.dto;

import com.scrapheap.account.model.LoginAttempt;
import com.scrapheap.account.model.Setting;
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

    private LoginAttempt loginAttempt;

    private Setting setting;


}



