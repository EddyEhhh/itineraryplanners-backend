package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class AccommodationDetailDTO {

    @NotEmpty
    private String address;

    @NotEmpty
    private Date checkIn;

    @NotEmpty
    private Date checkOut;

}
