package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    
}
