package com.scrapheap.itineraryplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ForgotPasswordEmailDTO {
    private String email;
}
