package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String airline;

    private int flightNumber;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;



}