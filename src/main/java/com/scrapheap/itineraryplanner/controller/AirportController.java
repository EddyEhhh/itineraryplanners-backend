package com.scrapheap.itineraryplanner.controller;


import com.scrapheap.itineraryplanner.dto.AirportIataCodeDTO;
import com.scrapheap.itineraryplanner.service.FlightService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/airport")
@Slf4j
public class AirportController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public ResponseEntity<?> get(@RequestBody @Valid AirportIataCodeDTO airportIataCodeDTO){
        return ResponseEntity.ok(flightService.getAirportDetails(airportIataCodeDTO.getIata()));
    }

}
