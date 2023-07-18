package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<ForgotPassword, Long> {

}
