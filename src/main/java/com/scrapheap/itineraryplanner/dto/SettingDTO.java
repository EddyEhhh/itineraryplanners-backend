package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SettingDTO {

    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Language must contain only alphanumeric characters")
    private String language;
}
