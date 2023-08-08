package com.scrapheap.itineraryplanner.dto;

import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.Itinerary;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TripDetailDTO {

    private Long id;

    private String title;

    private String location;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    private String currency;

    private double totalBudget;

    private String pictureLink;

    private boolean isPublic;

    private String lastUpdate;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch= FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(
            referencedColumnName = "id"
    )
    private List<ItineraryDetailDTO> itinerarys;

}
