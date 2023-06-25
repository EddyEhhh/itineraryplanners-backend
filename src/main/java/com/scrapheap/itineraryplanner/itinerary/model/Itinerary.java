package com.scrapheap.itineraryplanner.itinerary.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class Itinerary {
    @Id
    private Long itinerary_id;
    private int order;
    private String subheader;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "note",
            referencedColumnName = "note_id"
    )
    private List<Note> notes;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "ItemList",
            referencedColumnName = "itemList_id"
    )
    private List<ItemList> itemLists;

}