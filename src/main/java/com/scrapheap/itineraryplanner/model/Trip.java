package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String title;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private String currency;

    private double totalBudget;

    private String pictureLink;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(
            referencedColumnName = "id"
    )
    private List<Itinerary> itineraries;

    @ManyToOne
    @JoinColumn
    private Account account;


}