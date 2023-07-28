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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts/{username}/trips")
@Slf4j
public class TripController {


    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDetailDTO> createTrip(@RequestBody @Valid TripDetailDTO tripDetailDTO) {
        log.info(tripDetailDTO.toString());
        return new ResponseEntity<>(tripService.createTrip(tripDetailDTO), HttpStatus.CREATED);
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<List<TripDetailDTO>> getTrip(@PathVariable("username") String username){
//        return ResponseEntity.ok(tripService.getUserTripBasic(username));
//    }
    @GetMapping
    public ResponseEntity<List<TripDetailDTO>> getUserTrip(@PathVariable("username") String username){
        return ResponseEntity.ok(tripService.getUserTrip(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDetailDTO> getTrip(@PathVariable("username") String username, @PathVariable("id") Long id){
        return ResponseEntity.ok(tripService.getTripById(username, id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TripDetailDTO> updateTrip(@PathVariable("username") String username, @PathVariable("id") Long id, @RequestBody String tripDetailMap){
//        log.info(tripDetailMap);
//        return null;
                return ResponseEntity.ok(tripService.updateTrip(username, id, tripDetailMap));
    }
//    @GetMapping("/personal/{trip_id}")
//    public ResponseEntity<TripDetailDTO> getTripDetail(){
//        return ResponseEntity.ok(tripService.getUserTripBasic());
//    }

//    @GetMapping
//    public Page<String> getCodes(@PageableDefault(size = 5) Pageable pageable) {
////        int start = pageable.getOffset();
////        int end = (start + pageable.getPageSize()) > userIds.size() ? userIds.size() : (start + pageable.getPageSize());
////
////        return new PageImpl<String>(userIds.subList(start, end), pageable, userIds.size());
//        return null;
//    }

}
