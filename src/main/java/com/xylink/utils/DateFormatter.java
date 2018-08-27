package com.xylink.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

/**
 * Created by konglk on 2018/8/27.
 */
public class DateFormatter {

    public String format(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(millis/1000, 0, ZoneOffset.ofHours(8));
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(millis);
        Calendar today = Calendar.getInstance();
        if(DateUtils.isSameDay(today, target)) {
            int hour = target.get(Calendar.HOUR);
            int minutes = target.get(Calendar.MINUTE);
            return hour+":"+minutes;
        }
        
        return "";
    }

}
