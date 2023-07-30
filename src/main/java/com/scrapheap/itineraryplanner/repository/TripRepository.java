package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.ForgotPassword;
import com.scrapheap.itineraryplanner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    public List<Trip> findByAccount(Account account);

}
