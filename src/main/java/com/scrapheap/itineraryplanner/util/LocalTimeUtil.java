package com.scrapheap.itineraryplanner.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class LocalTimeUtil {



    public static LocalTime parseTime(String timeString){
        return LocalTime.parse(timeString);
    }



}
