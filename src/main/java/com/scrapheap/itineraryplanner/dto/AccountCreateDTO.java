package com.scrapheap.itineraryplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountCreateDTO {


    private String displayName;

    private String email;

    private String username;

    private String password;



}



