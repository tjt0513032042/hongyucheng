package com.hongyuecheng.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2017/11/19.
 */
public class DateUtil {

    public static String FORMAT_TYPE_DEFAUL = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_TYPE_1 = "yyyy-MM-dd";
    public static String FORMAT_TYPE_2 = "HH:mm:ss";

    public static Date parse(String date, String formatType) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        String format = FORMAT_TYPE_DEFAUL;
        if (StringUtils.isNotEmpty(formatType)) {
            format = formatType;
        }
        Date result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.parse(date);
        } catch (ParseException e) {
            result = null;
        }
        return result;
    }

    public static Date parse(String date) {
        return parse(date);
    }

    public static Date addDays(Date date, int num) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int num) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    public static String format(Date date) {
        return format(date, FORMAT_TYPE_DEFAUL);
    }

    public static String format(Date date, String format) {
        if (null == date) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
        }
        return "";
    }
}
