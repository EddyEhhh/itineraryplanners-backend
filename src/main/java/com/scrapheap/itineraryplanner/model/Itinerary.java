package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int order;

    private String subheader;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "note",
            referencedColumnName = "id"
    )
    private List<Note> notes;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "ItemList",
            referencedColumnName = "id"
    )
    private List<ItemList> itemLists;

}