package com.scrapheap.itineraryplanner.dto;

import com.scrapheap.itineraryplanner.model.LoginAttempt;
import com.scrapheap.itineraryplanner.model.Setting;
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

    private String imageId;

    private LocalDateTime created;

    private LoginAttempt loginAttempt;

    private Setting setting;


}



