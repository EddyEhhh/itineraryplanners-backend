package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.ItineraryDetailDTO;
import com.scrapheap.itineraryplanner.dto.PlaceDetailDTO;
import com.scrapheap.itineraryplanner.dto.TripDetailDTO;
import com.scrapheap.itineraryplanner.model.*;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.TripRepository;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import com.scrapheap.itineraryplanner.util.LocalDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private AccountRepository accountRepository;

//    public List<Trip> getAllTripFromUsername(String username){
//
//
//    }

//    public TripDetailDTO updateTrip(TripDetailDTO){
//
//    }

    public TripDetailDTO createBasicTrip(TripDetailDTO tripDetailDTO){
        Trip trip = Trip.builder()
                .title(tripDetailDTO.getTitle())
                .location(tripDetailDTO.getLocation())
                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
                .currency(tripDetailDTO.getCurrency())
                .totalBudget(tripDetailDTO.getTotalBudget())
                .pictureLink(tripDetailDTO.getPictureLink())
                .build();

        tripRepository.save(trip);
        return tripDetailDTO;
    }

    public TripDetailDTO createTrip(TripDetailDTO tripDetailDTO){
//        ArrayList<Itinerary> itinerarys = new ArrayList<>();

        Trip trip = Trip.builder()
                .title(tripDetailDTO.getTitle())
                .location(tripDetailDTO.getLocation())
                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
                .currency(tripDetailDTO.getCurrency())
                .totalBudget(tripDetailDTO.getTotalBudget())
                .pictureLink(tripDetailDTO.getPictureLink())
                .build();

        if(tripDetailDTO.getItinerary().size() > 0){
            List<Itinerary> itineraryList = itineraryService.convertToEntityList(tripDetailDTO.getItinerary());
            trip.setItineraries(itineraryList);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        trip.setAccount(account);

        tripRepository.save(trip);
        return tripDetailDTO;
    }






}
