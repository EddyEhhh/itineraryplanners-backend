package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Place{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private int itineraryOrder;

    @NotEmpty
    private String location;


    private LocalTime timeStart;


    private LocalTime timeEnd;


    private String note;


    private String pictureLink;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(
            referencedColumnName = "id")
    private Flight flight;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(
            referencedColumnName = "id")
    private Accommodation accommodation;

//    @OneToMany(cascade=CascadeType.ALL,
//            fetch=FetchType.LAZY,
//            orphanRemoval = true)
//    @JoinColumn(referencedColumnName = "id")
//    private List<Flight> flights;
//
//    @OneToMany(cascade=CascadeType.ALL,
//            fetch=FetchType.LAZY,
//            orphanRemoval = true)
//    @JoinColumn(referencedColumnName = "id")
//    private List<Accommodation> accommodations;

}
