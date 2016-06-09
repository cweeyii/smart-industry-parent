package com.cweeyii.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static final String DefaultShortFormat = "yyyy-MM-dd";
    public static final String DefaultLongFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String DefaultMinuteFormat = "yyyy-MM-dd HH:mm";

    public DateUtil() {
    }

    public static Date string2DateDay(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        str = StringUtil.null2Trim(str);

        try {
            return formatter.parse(str);
        } catch (ParseException var4) {
            Calendar cal = Calendar.getInstance();
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static Date string2DateDay4Exception(String str) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        str = StringUtil.null2Trim(str);
        return formatter.parse(str);
    }

    public static Date string2DateSecond24(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        str = StringUtil.null2Trim(str);
        String matchstr = "[0-2]\\d\\d\\d-\\d\\d-\\d\\d [0-2]\\d:[0-6]\\d";
        if(StringUtil.regexMatch(str, matchstr)) {
            str = str + ":00";
        }

        try {
            return formatter.parse(str);
        } catch (ParseException var4) {
            return new Date();
        }
    }

    public static Date stirng2DateMinute(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        str = StringUtil.null2Trim(str);

        try {
            return formatter.parse(str);
        } catch (ParseException var3) {
            return new Date();
        }
    }

    public static Date stirng2Date(String str, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);
        str = StringUtil.null2Trim(str);

        try {
            return formatter.parse(str);
        } catch (ParseException var4) {
            return new Date();
        } catch (IllegalArgumentException var5) {
            System.out.println("format string Illegal:" + formatString);
            return null;
        }
    }

    public static Date string2Date4Null(String str, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);
        str = StringUtil.null2Trim(str);

        try {
            return formatter.parse(str);
        } catch (Exception var4) {
            return null;
        }
    }

    public static String Date2String(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static Integer Date2IntDay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return Integer.valueOf(Integer.parseInt(formatter.format(date)));
    }

    public static String secondsToString(Integer seconds) {
        return Date2String(fromUnixTime(seconds));
    }

    public static String Date2StringMin(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public static String Date2StringSec(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String Date2StringSec(Integer seconds) {
        return Date2StringSec(fromUnixTime(seconds));
    }

    public static String Date2String(Date date, String formatString) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(formatString);
            return e.format(date);
        } catch (IllegalArgumentException var3) {
            System.out.println("format string Illegal:" + formatString);
            return "";
        }
    }

    public static int unixTime() {
        return (int)(System.currentTimeMillis() / 1000L);
    }

    public static Date fromUnixTime(Integer seconds) {
        return new Date((long)seconds.intValue() * 1000L);
    }

    public static Date today() {
        return toDay(new Date());
    }

    public static Date toDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date toNight(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static List<String> dateBetween(String startDateStr, String endDateStr) {
        ArrayList dateList = new ArrayList();
        Date endDate = string2DateDay(endDateStr);
        Date startDate = string2DateDay(startDateStr);
        long day = (endDate.getTime() - startDate.getTime()) / 86400000L;
        Calendar cal = Calendar.getInstance();

        for(int i = 0; (long)i <= day; ++i) {
            cal.setTime(startDate);
            cal.add(5, i);
            dateList.add(Date2String(cal.getTime()));
        }

        return dateList;
    }

    public static Date toYesterday(Date date) {
        return add(date, 6, -1);
    }

    public static Date toTommorow(Date date) {
        return add(date, 6, 1);
    }

    static Date add(Date date, int field, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(field, value);
        return cal.getTime();
    }

    public static int getMonthDays(Integer year, Integer month) {
        Calendar c = Calendar.getInstance();
        c.set(year.intValue(), month.intValue() - 1, 1);
        return c.getActualMaximum(5);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        return cal.get(1);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        return cal.get(2) + 1;
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        int dayOfWeek = cal.get(7);
        boolean rel = false;
        byte rel1;
        switch(dayOfWeek) {
        case 2:
            rel1 = 1;
            break;
        case 3:
            rel1 = 2;
            break;
        case 4:
            rel1 = 3;
            break;
        case 5:
            rel1 = 4;
            break;
        case 6:
            rel1 = 5;
            break;
        case 7:
            rel1 = 6;
            break;
        default:
            rel1 = 7;
        }

        return rel1;
    }

    public static int day2Unixtime(String day) {
        return (int)(string2DateDay(day).getTime() / 1000L);
    }

    public static int date2Unixtime(Date date) {
        return (int)(date.getTime() / 1000L);
    }

    public static Date toTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(1, 1970);
        cal.set(2, 0);
        cal.set(5, 1);
        return cal.getTime();
    }
}
