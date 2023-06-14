package com.scrapheap.account.util;

import java.time.LocalDateTime;

public final class LocalDateTimeUtil {

    private LocalDateTimeUtil(){}

    public static LocalDateTime calculateExpirationTimestamp(int expiryDurationInMinute){
        LocalDateTime time = LocalDateTime.now();
        time = time.plusMinutes(expiryDurationInMinute);
        return time;
    }

}
