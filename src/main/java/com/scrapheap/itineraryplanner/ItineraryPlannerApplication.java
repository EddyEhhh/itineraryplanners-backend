package com.scrapheap.itineraryplanner;


import com.scrapheap.itineraryplanner.s3.S3Buckets;
import com.scrapheap.itineraryplanner.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableAsync
public class ItineraryPlannerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ItineraryPlannerApplication.class, args);
    }


}
