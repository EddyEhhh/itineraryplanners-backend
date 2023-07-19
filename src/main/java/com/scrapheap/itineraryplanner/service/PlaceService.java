package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.PlaceDetailDTO;
import com.scrapheap.itineraryplanner.model.Accommodation;
import com.scrapheap.itineraryplanner.model.Flight;
import com.scrapheap.itineraryplanner.model.Place;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import com.scrapheap.itineraryplanner.util.LocalTimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceService {

    public List<Place> convertToEntityList(List<PlaceDetailDTO> placeDetailDTOs){

        if(placeDetailDTOs.size() == 0 || placeDetailDTOs == null){
            return null;
        }

        ArrayList<Place> placeList = new ArrayList<Place>();

        int itineraryOrder = 0;

        for(PlaceDetailDTO eachPlaceDetail : placeDetailDTOs){
            Place place = Place.builder()
                    .itineraryOrder(itineraryOrder++)
                    .location(eachPlaceDetail.getLocation())
                    .timeStart(LocalTimeUtil.parseTime(eachPlaceDetail.getTimeStart()))
                    .timeEnd(LocalTimeUtil.parseTime(eachPlaceDetail.getTimeEnd()))
                    .note(eachPlaceDetail.getNote())
                    .pictureLink(eachPlaceDetail.getPictureLink())
                    .build();

            if(eachPlaceDetail.getFlight() != null){
                Flight flight = Flight.builder()
                        .airline(eachPlaceDetail.getFlight().getAirline())
                        .flightNumber(eachPlaceDetail.getFlight().getFlightNumber())
                        .departureDate(LocalDateTimeUtil.parseDateTime(eachPlaceDetail.getFlight().getDepartureDate()))
                        .arrivalDate(LocalDateTimeUtil.parseDateTime(eachPlaceDetail.getFlight().getArrivalDate()))
                        .build();

                place.setFlight(flight);

            }else if(eachPlaceDetail.getAccommodation() != null){
                Accommodation accommodation = Accommodation.builder()
                        .address(eachPlaceDetail.getAccommodation().getAddress())
                        .checkIn(LocalDateTimeUtil.parseDateTime(eachPlaceDetail.getAccommodation().getCheckIn()))
                        .checkOut(LocalDateTimeUtil.parseDateTime(eachPlaceDetail.getAccommodation().getCheckOut()))
                        .build();

                place.setAccommodation(accommodation);
            }

            placeList.add(place);
        }
        return placeList;
    }

}
