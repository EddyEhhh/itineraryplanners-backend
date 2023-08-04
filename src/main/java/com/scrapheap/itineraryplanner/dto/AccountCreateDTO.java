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


    @NotBlank(message = "authenticate.error.validation.display_name.blank")
    private String displayName;

    @Email(message = "authenticate.error.validation.email.invalid")
    @NotBlank(message = "authenticate.error.validation.email.blank")
    private String email;

    @NotBlank(message = "authenticate.error.validation.username.blank")
    private String username;

    @NotBlank(message = "authenticate.error.validation.password.blank")
    private String password;

}



