package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.dto.AirportDetailDTO;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.Airport;
import com.scrapheap.itineraryplanner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    public Airport findByIata(String iata);

}
