package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FlightUpdateDTO {

    @NotEmpty
    private String airline;

    @NotEmpty
    private int flightNumber;

    @NotEmpty
    private String departureDate;



}
