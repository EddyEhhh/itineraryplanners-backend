package com.scrapheap.itineraryplanner.dto;

import com.scrapheap.itineraryplanner.model.Accommodation;
import com.scrapheap.itineraryplanner.model.Flight;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PlaceDetailDTO {

//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    private Long id;

//    @NotEmpty
//    private int itineraryOrder;

    private String location;

    private LocalDateTime timeStart;

    private LocalDateTime timeEnd;

    private String note;

    private String pictureLink;

    @OneToOne(cascade= CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            referencedColumnName = "id")
    private FlightDetailDTO flightDetailDTO;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            referencedColumnName = "id")
    private AccommodationDetailDTO accommodationDetailDTO;

}
