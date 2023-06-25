package com.scrapheap.itineraryplanner.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableAsync
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
