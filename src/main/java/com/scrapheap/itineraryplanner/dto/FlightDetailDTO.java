package com.scrapheap.itineraryplanner.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class FlightDetailDTO {

    @NotEmpty
    private String airline;

    @NotEmpty
    private int flightNumber;

    @NotEmpty
    private Date departureDate;

    @NotEmpty
    private Date arrivalDate;

}
