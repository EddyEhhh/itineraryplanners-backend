package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class ItemList {
    @Id
    private Long id;
    private int itineraryOrder;
    private String location;
    private String note;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            referencedColumnName = "id"
    )
    private List<Item> items;

}
