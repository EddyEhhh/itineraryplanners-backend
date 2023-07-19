package com.scrapheap.itineraryplanner.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private LocalDateTimeUtil(){}

    public static LocalDateTime calculateExpirationTimestamp(int expiryDurationInMinute){
        LocalDateTime time = LocalDateTime.now();
        time = time.plusMinutes(expiryDurationInMinute);
        return time;
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);

    }



}
