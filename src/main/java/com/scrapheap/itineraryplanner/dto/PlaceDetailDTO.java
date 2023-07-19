package com.scrapheap.itineraryplanner.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PlaceDetailDTO {

//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    private Long id;

//    @NotEmpty
//    private int itineraryOrder;


    @NotEmpty(message = "Cannot be empty test!!!")
    private String location;

    private String timeStart;

    private String timeEnd;

    private String note;

    private String pictureLink;

    @OneToOne(cascade= CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            referencedColumnName = "id")
    private FlightDetailDTO flight;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            referencedColumnName = "id")
    private AccommodationDetailDTO accommodation;

}
