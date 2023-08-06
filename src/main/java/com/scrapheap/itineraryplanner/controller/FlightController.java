package com.scrapheap.itineraryplanner.controller;

import com.scrapheap.itineraryplanner.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/accounts/{username}/trips/{tripId}/flights")
@Slf4j
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public ResponseEntity<?> get(){
        return ResponseEntity.ok(flightService.getFlightDetails("SQ", "706", "2023-12-24"));
    }



}
