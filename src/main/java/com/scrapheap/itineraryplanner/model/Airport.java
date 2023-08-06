package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {

    @Id
    private long id;

    private String iata;

    private String icao;

    private String name;

    private String location;

    private String street_number;

    private String street;

    private String city;

    private String county;

    private String state;

    private String country_iso;

    private String country;

    private String postal_code;

    private String phone;

    private double latitude;

    private double longitude;

    private int uct;

    private String website;

}