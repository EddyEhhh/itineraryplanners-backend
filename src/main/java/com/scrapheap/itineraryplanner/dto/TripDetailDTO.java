package com.scrapheap.itineraryplanner.dto;

import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.Itinerary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TripDetailDTO {

    private Long id;

    private String title;

    private String location;

    private Date startDate;

    private Date endDate;

    private String currency;

    private double totalBudget;

    private String pictureLink;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch= FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(
            referencedColumnName = "id"
    )
    private List<ItineraryDetailDTO> itineraryDetailsDTO;

}
