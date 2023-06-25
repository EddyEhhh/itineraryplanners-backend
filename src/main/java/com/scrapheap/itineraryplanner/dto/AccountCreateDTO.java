package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountCreateDTO {


    @NotBlank(message = "authenticate.validation.error.display_name.blank")
    private String displayName;

    @Email(message = "authenticate.validation.error.email.invalid")
    private String email;

    @NotBlank(message = "authenticate.validation.error.username.blank")
    private String username;

    @NotBlank(message = "authenticate.validation.error.password.blank")
    private String password;

}



