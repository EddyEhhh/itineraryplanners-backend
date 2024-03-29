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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/accounts/{username}/trips")
@Slf4j
public class TripController {


    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDetailDTO> create(@RequestBody @Valid TripDetailDTO tripDetailDTO) {
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

    @GetMapping("/upcoming")
    public ResponseEntity<List<TripDetailDTO>> getUserUpcomingTrip(@PathVariable("username") String username){
        return ResponseEntity.ok(tripService.getUserUpcomingTrip(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDetailDTO> get(@PathVariable("username") String username, @PathVariable("id") Long id){
        log.info("ID RECIEVED: " + id);
        return ResponseEntity.ok(tripService.getTripById(username, id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TripDetailDTO> update(@PathVariable("username") String username, @PathVariable("id") Long id, @RequestBody String tripDetailMap){
        return ResponseEntity.ok(tripService.updateTrip(username, id, tripDetailMap));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDetailDTO> updateOverride(@PathVariable("username") String username, @PathVariable("id") Long id, @RequestBody @Valid TripDetailDTO tripDetailDTO){
        return ResponseEntity.ok(tripService.updateTrip(username, id, tripDetailDTO));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<TripDetailDTO> delete(@PathVariable("username") String username, @PathVariable("id") Long id){
        return ResponseEntity.ok(tripService.deleteTrip(username, id));
    }

    @PostMapping(value = "/{id}/imageUpload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadTripImage(@PathVariable("username") String username,
                                             @PathVariable("id") long id,
                                                @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImageResponse = tripService.uploadTripImage(username, id, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImageResponse);
    }

    @GetMapping(value = "/{id}/imageRetrieve"
    )
    public String retrieveTripImage(@PathVariable("username") String username,
                                    @PathVariable("id") long id) throws IOException{
        return tripService.getTripImage(username, id);
    }


    @DeleteMapping("/{id}/imageDelete")
    public ResponseEntity<?> deleteProfileImage(@PathVariable("username") String username,
                                        @PathVariable("id") long id) {
        String response = tripService.deleteTripImage(username, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
