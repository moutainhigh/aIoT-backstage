package com.aiot.aiotbackstage.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static boolean isValid(String date) {
        return isValid(FORMAT_PATTERN);
    }

    public static boolean isValid(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String format(Date date) {
        return format(date, FORMAT_PATTERN);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat to = new SimpleDateFormat(pattern);
        return to.format(date);
    }
}
