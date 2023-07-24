package com.scrapheap.itineraryplanner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableAsync
public class ItineraryPlannerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ItineraryPlannerApplication.class, args);
    }


}
