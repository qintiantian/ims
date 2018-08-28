package com.xylink.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by konglk on 2018/8/27.
 */
public class DateFormatter {

    public static String format(Long millis) {
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(millis);
        Calendar today = Calendar.getInstance();
        if(DateUtils.isSameDay(today, target)) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            return sdf.format(target.getTime());
        }
        today.add(Calendar.DAY_OF_MONTH, -1);
        Calendar yesterday = today;
        if(DateUtils.isSameDay(yesterday, target)) {
//            return "昨天";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
        return sdf.format(target.getTime());
    }

    public static String format(Object millis) {
        try {
            return format(Long.valueOf(millis.toString()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("mills must be timestamp");
        }
    }

}
