package com.scrapheap.itineraryplanner.account.util;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Service
public class StringUtil {

    public String getStringFromFile(String file) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        return contentBuilder.toString();
    }

}
