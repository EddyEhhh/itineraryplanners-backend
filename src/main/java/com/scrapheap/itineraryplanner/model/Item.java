package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Item {

    @Id
    private Long id;
    private int itemOrder;
    private String location;
    private String note;


}