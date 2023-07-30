package com.scrapheap.itineraryplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ForgotPasswordDTO {
    private String token;
    private String newPassword;
}
