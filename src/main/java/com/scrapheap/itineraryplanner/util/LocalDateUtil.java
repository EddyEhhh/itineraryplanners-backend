package com.scrapheap.itineraryplanner.util;

import java.time.LocalDate;

public final class LocalDateUtil {



    public static LocalDate parseDate(String DateString){
        return LocalDate.parse(DateString);
    }



}
