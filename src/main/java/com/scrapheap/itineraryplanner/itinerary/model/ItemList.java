package com.scrapheap.itineraryplanner.itinerary.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class ItemList {
    @Id
    private Long itemList_id;
    private int itineraryOrder;
    private String location;
    private String note;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "Item",
            referencedColumnName = "item_id"
    )
    private List<Item> items;

}
