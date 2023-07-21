package com.scrapheap.itineraryplanner.controller;


import com.scrapheap.itineraryplanner.dto.AccountCredentialDTO;
import com.scrapheap.itineraryplanner.dto.TripDetailDTO;
import com.scrapheap.itineraryplanner.model.Trip;
import com.scrapheap.itineraryplanner.service.TripService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@Slf4j
public class TripController {


    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDetailDTO> createTrip(@RequestBody @Valid TripDetailDTO tripDetailDTO) {
        log.info(tripDetailDTO.toString());
        return ResponseEntity.ok(tripService.createTrip(tripDetailDTO));
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<List<TripDetailDTO>> getTrip(@PathVariable("username") String username){
//        return ResponseEntity.ok(tripService.getUserTripBasic(username));
//    }
    @GetMapping("/personal/all")
    public ResponseEntity<List<TripDetailDTO>> getTrip(){
        return ResponseEntity.ok(tripService.getUserTripBasic());
    }

//    @GetMapping("/personal/{trip_id}")
//    public ResponseEntity<TripDetailDTO> getTripDetail(){
//        return ResponseEntity.ok(tripService.getUserTripBasic());
//    }

    @GetMapping
    public Page<String> getCodes(@PageableDefault(size = 5) Pageable pageable) {
//        int start = pageable.getOffset();
//        int end = (start + pageable.getPageSize()) > userIds.size() ? userIds.size() : (start + pageable.getPageSize());
//
//        return new PageImpl<String>(userIds.subList(start, end), pageable, userIds.size());
        return null;
    }

}
