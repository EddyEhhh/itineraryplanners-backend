package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
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

    @NotEmpty
    private int itineraryOrder;

    @NotEmpty
    private String location;


    private LocalDateTime timeStart;


    private LocalDateTime timeEnd;


    private String note;


    private String pictureLink;

    @OneToMany(cascade=CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(referencedColumnName = "id")
    private List<Flight> flights;

    @OneToMany(cascade=CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(referencedColumnName = "id")
    private List<Accommodation> accommodations;

}
