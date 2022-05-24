package com.andreamartz.jsontestemulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DateTime {
    private final ZonedDateTime tzInstance;
    private final long milliseconds_since_epoch;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public DateTime(ZonedDateTime tzInstance) {
        this.tzInstance = ZonedDateTime.now(ZoneId.of("GMT"));
        this.milliseconds_since_epoch = System.currentTimeMillis();
//        return
    }

}


//@RestController
//@RequestMapping("/")
//public class JsontestController {
//
//    @GetMapping("/datetime")
//    HashMap<String, String> datetime() throws UnknownHostException {
//        HashMap<String, String> dateTimeHash = new HashMap<>();
//
//        ZonedDateTime tzInstance = ZonedDateTime.now(ZoneId.of("GMT"));
//
//        DateTimeFormatter dateFormatter
//                = DateTimeFormatter.ofPattern("MM-dd-yyyy");
//        String formattedDateString = dateFormatter.format(tzInstance);
//
//        DateTimeFormatter timeFormatter
//                = DateTimeFormatter.ofPattern("hh:mm:ss");
//        String formattedTimeString = timeFormatter.format(tzInstance);
//
//        Instant instant = Instant.now();
//        long epochValue = instant.getEpochSecond();
//        long ms = System.currentTimeMillis();
//
//        String milliseconds = String.format("%d", ms);
//
//        dateTimeHash.put("date", formattedDateString);
//        dateTimeHash.put("time", formattedTimeString);
//        dateTimeHash.put("milliseconds", milliseconds);
//        return dateTimeHash;
//    }

