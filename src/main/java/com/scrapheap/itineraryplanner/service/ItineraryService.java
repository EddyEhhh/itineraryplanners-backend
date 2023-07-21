package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.ItineraryDetailDTO;
import com.scrapheap.itineraryplanner.model.Itinerary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryService {

    @Autowired
    private PlaceService placeService;

    public List<Itinerary> convertToEntityList(List<ItineraryDetailDTO> itineraryDetailDTOs){

        if(itineraryDetailDTOs.size() == 0 || itineraryDetailDTOs == null){
            return null;
        }

        ArrayList<Itinerary> itineraryList = new ArrayList<Itinerary>();

        int order = 0;

        for(ItineraryDetailDTO itineraryDetailDTO : itineraryDetailDTOs) {
            Itinerary itinerary = Itinerary.builder()
                    .orderNumber(order++)
                    .subheader(itineraryDetailDTO.getSubheader())
                    .places(placeService.convertToEntityList(itineraryDetailDTO.getPlace()))
                    .build();

            itineraryList.add(itinerary);

        }
        return itineraryList;

    }

    public List<ItineraryDetailDTO> convertToDTOList(List<Itinerary> itinerarys) {

        if(itinerarys.size() == 0 || itinerarys == null){
            return null;
        }

        ArrayList<ItineraryDetailDTO> itineraryListDTO = new ArrayList<ItineraryDetailDTO>();

        for(Itinerary itinerary : itinerarys) {
            ItineraryDetailDTO itineraryDetailDTO = ItineraryDetailDTO.builder()
                    .subheader(itinerary.getSubheader())
                    .place(placeService.convertToDTOList(itinerary.getPlaces()))
                    .build();
            itineraryListDTO.add(itineraryDetailDTO);

        }
        return itineraryListDTO;
    }
}
