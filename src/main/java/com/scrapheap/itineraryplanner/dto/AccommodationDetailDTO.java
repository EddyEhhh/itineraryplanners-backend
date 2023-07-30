package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class AccommodationDetailDTO {

    @NotEmpty
    private String address;

    @NotEmpty
    private String checkIn;

    @NotEmpty
    private String checkOut;

}
