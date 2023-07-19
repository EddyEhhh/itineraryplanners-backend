package com.scrapheap.itineraryplanner.controller;


import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.TripDetailDTO;
import com.scrapheap.itineraryplanner.service.TripService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trip")
@Slf4j
public class TripController {


    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDetailDTO> createTrip(@RequestBody @Valid TripDetailDTO tripDetailDTO) {
        log.info(tripDetailDTO.toString());
        return ResponseEntity.ok(tripService.createTrip(tripDetailDTO));
    }

}
