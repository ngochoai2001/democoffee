package com.example.demo.utils;

import com.nimbusds.oauth2.sdk.util.date.SimpleDate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static long getDaysBetween(Date startDate, Date endDate) {
        LocalDate startLocalDate = Instant.ofEpochMilli(startDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endLocalDate = Instant.ofEpochMilli(endDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }
    public static String dateFormat(Date date){
        SimpleDateFormat sdf =  new SimpleDateFormat(DD_MM_YYYY);
        return sdf.format(date);
    }
}
