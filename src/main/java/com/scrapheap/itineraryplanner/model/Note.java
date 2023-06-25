package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Note {

    @Id
    private Long id;
    private int itineraryOrder;
    private String note;

}