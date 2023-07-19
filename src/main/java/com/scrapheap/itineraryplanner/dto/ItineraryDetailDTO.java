package com.scrapheap.itineraryplanner.dto;

import com.scrapheap.itineraryplanner.model.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItineraryDetailDTO {

//    private Long id;

//    private int order;

    private String subheader;

    private List<PlaceDetailDTO> place;

}
