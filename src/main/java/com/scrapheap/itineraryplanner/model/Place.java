package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Place{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private int itineraryOrder;

    @NotEmpty
    private String location;


    private LocalDateTime timeStart;


    private LocalDateTime timeEnd;


    private String note;


    private String pictureLink;

    @OneToMany(cascade=CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "flights", referencedColumnName = "id")
    private List<Flight> flights;

    @OneToMany(cascade=CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "accommodations", referencedColumnName = "id")
    private List<Accommodation> accommodations;


    public void addFlight(Flight flight){
        this.flights.add(flight);
    }

    public void removeFlight(Flight flight){
        this.flights.remove(flight);
    }

    public void addAccommodation(Accommodation accommodation){
        this.accommodations.add(accommodation);
    }

    public void removeAccommodation(Accommodation accommodation){
        this.accommodations.remove(accommodation);
    }

    public Place(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Place{" +
                "location='" + location + '\'' +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", note='" + note + '\'' +
                '}';
    }
}
