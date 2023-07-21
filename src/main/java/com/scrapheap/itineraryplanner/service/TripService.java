package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.AccountDetailDTO;
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

//    public TripDetailDTO createBasicTrip(TripDetailDTO tripDetailDTO){
//        Trip trip = Trip.builder()
//                .title(tripDetailDTO.getTitle())
//                .location(tripDetailDTO.getLocation())
//                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
//                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
//                .currency(tripDetailDTO.getCurrency())
//                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
//                .build();
//
//        tripRepository.save(trip);
//        return tripDetailDTO;
//    }

    public List<TripDetailDTO> getUserTripBasic(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        return getUserTripBasic(username);
    }

    public List<TripDetailDTO> getUserTripBasic(String username){
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        return convertToDTOList(tripRepository.findByAccount(account));
    }


    public List<TripDetailDTO> convertToDTOList(List<Trip> trips){
        List<TripDetailDTO> tripDetailDTOS = new ArrayList<>();

        if(trips == null || trips.size() == 0){
            return null;
        }

        for(Trip trip : trips){
            tripDetailDTOS.add(convertToDTO(trip));
        }

        return tripDetailDTOS;
    }

    public TripDetailDTO convertToDTO(Trip trip){
        TripDetailDTO tripDetailDTO = TripDetailDTO.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .location(trip.getLocation())
                .startDate(trip.getStartDate().toString())
                .endDate(trip.getEndDate().toString())
                .currency(trip.getCurrency())
                .totalBudget(trip.getTotalBudget())
                .pictureLink(trip.getPictureLink())
                .build();

        if(trip.getItineraries().size() > 0){
            List<ItineraryDetailDTO> itineraryDTOList = itineraryService.convertToDTOList(trip.getItineraries());
            tripDetailDTO.setItinerary(itineraryDTOList);
        }

        return tripDetailDTO;
    }

    public Trip convertToEntity(TripDetailDTO tripDetailDTO){
        Trip trip = Trip.builder()
                .title(tripDetailDTO.getTitle())
                .location(tripDetailDTO.getLocation())
                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
                .currency(tripDetailDTO.getCurrency())
                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
                .build();

        if(tripDetailDTO.getItinerary().size() > 0){
            List<Itinerary> itineraryList = itineraryService.convertToEntityList(tripDetailDTO.getItinerary());
            trip.setItineraries(itineraryList);
        }

        return trip;
    }

    public TripDetailDTO createTrip(TripDetailDTO tripDetailDTO){
//        ArrayList<Itinerary> itinerarys = new ArrayList<>();
        Trip trip = convertToEntity(tripDetailDTO);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        trip.setAccount(account);

        tripRepository.save(trip);
        return tripDetailDTO;
    }

//    public TripDetailDTO createTrip(TripDetailDTO tripDetailDTO, AccountDetailDTO accountDetailDTO){
////        ArrayList<Itinerary> itinerarys = new ArrayList<>();
//
//        Trip trip = Trip.builder()
//                .title(tripDetailDTO.getTitle())
//                .location(tripDetailDTO.getLocation())
//                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
//                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
//                .currency(tripDetailDTO.getCurrency())
//                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
//                .build();
//
//        if(tripDetailDTO.getItinerary().size() > 0){
//            List<Itinerary> itineraryList = itineraryService.convertToEntityList(tripDetailDTO.getItinerary());
//            trip.setItineraries(itineraryList);
//        }
//
//        Account account = accountRepository.findByUsernameAndIsDeletedFalse(accountDetailDTO.getUsername());
//        trip.setAccount(account);
//
//        tripRepository.save(trip);
//        return tripDetailDTO;
//    }

}

