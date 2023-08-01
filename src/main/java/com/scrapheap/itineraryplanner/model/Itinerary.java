package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int orderNumber;

    private String subheader;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch=FetchType.EAGER,
            orphanRemoval = true

    )
    @JoinColumn(
            referencedColumnName = "id"
    )
    private List<Place> places;



//    @OneToMany(
//            cascade = CascadeType.ALL
//    )
//    @JoinColumn(
//            referencedColumnName = "id"
//    )
//    private List<Note> notes;

//    @OneToMany(
//            cascade = CascadeType.ALL
//    )
//    @JoinColumn(
//            referencedColumnName = "id"
//    )
//    private List<ItemList> itemLists;

}