package com.weather.util;

import java.util.Date;

/**
 * @author akif
 * @since 1/14/22
 */
public class DateUtils {

    public static Date getDateFromTimestamp(Long timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null!");
        }

        return new Date(timestamp * 1000);
    }

    public static long getTimestampFromDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null!");
        }

        return date.getTime() / 1000;
    }

    public static long getNextDayTimestamp(long currentDay) {
        return currentDay + 24 * 60 * 60;
    }
}
