package com.scrapheap.itineraryplanner.itinerary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Note {

    @Id
    private Long note_id;
    private int itineraryOrder;
    private String note;

}