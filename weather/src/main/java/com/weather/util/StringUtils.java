package com.weather.util;


import java.util.Arrays;

/**
 * @author akif
 * @since 1/13/22
 */
public class StringUtils {
    public static String nullSafeString(String str) {
        return isNotEmpty(str) ? str : "";
    }

    public static boolean isNotEmpty(String... strs) {
        if (strs.length == 0) {
            throw new IllegalArgumentException("Invalid Param!");
        }

        return Arrays.stream(strs).noneMatch(StringUtils::isEmpty);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
