package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FlightDetailDTO {

    @NotEmpty
    private String airline;

    private int flightNumber;

    @NotEmpty
    private String departureDate;

    @NotEmpty
    private String arrivalDate;

}
