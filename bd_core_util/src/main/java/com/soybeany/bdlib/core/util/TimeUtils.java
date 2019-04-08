package com.soybeany.bdlib.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * <br> Created by Soybeany on 16/2/17.
 */
public class TimeUtils {

    // 时间长度
    public static final long SECOND = 1000L; // 一秒
    public static final long MINUTE = 60 * SECOND; // 一分钟
    public static final long HOUR = 60 * MINUTE; // 一小时
    public static final long DAY = 24 * HOUR; // 一天
    public static final long WEEK = 7 * DAY; // 一周
    public static final long MONTH_HALF = 2 * WEEK + DAY; // 半个月
    public static final long MONTH = 2 * MONTH_HALF; // 一个月

    // 时间格式
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_DAY = "dd";
    public static final String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_yyyy_MM_dd_HH_mm_ss2 = "yyyy-MM-dd-HH-mm-ss";
    public static final String FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_TIME = "yyyy年MM月dd日  HH时mm分";


    /**
     * 获取当前时间
     */
    public static String getCurrentTime(String format) {
        return getTime(getCurrentTimeStamp(), format);
    }

    /**
     * 获取当前时间戳(单位：毫秒)
     */
    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 将指定格式的时间转换为时间戳
     *
     * @return 若格式转换失败，则会返回-1
     */
    public static long getTimeStamp(String time, String format) {
        try {
            return getDateFormat(format).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获得格式器
     */
    public static DateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.CHINA);
    }

    /**
     * 根据时间戳返回指定格式的时间
     */
    public static String getTime(long timeStamp, String format) {
        return getTime(new Date(timeStamp), format);
    }

    /**
     * 根据日期返回指定格式的时间
     */
    public static String getTime(Date date, String format) {
        return getDateFormat(format).format(date);
    }

    /**
     * 根据时间戳与日期偏移值返回指定格式的时间
     */
    public static String getTime(long timeStamp, int delta, String format) {
        return getTime(timeStamp, delta, getDateFormat(format));
    }

    /**
     * 根据年份的偏移值返回年份列表
     *
     * @param startYear 开始年份
     * @param addition  额外添加的年份
     */
    public static ArrayList<String> getYearsList(int startYear, int addition) {
        ArrayList<String> years = new ArrayList<>();
        int curYear = Integer.parseInt(TimeUtils.getTime(TimeUtils.getCurrentTimeStamp(), TimeUtils.FORMAT_YEAR));
        do {
            years.add(startYear + "");
            startYear++;
        } while (startYear <= curYear + addition);
        return years;
    }

    /**
     * 根据时间戳与日期偏移值返回指定格式的时间列表
     */
    public static ArrayList<String> getTimeList(long timeStamp, int start, int end, String format) {
        ArrayList<String> dates = new ArrayList<>();
        DateFormat dateFormat = getDateFormat(format);
        for (int i = start; i <= end; i++) {
            dates.add(TimeUtils.getTime(timeStamp, i, dateFormat));
        }
        return dates;
    }

    /**
     * 获得指定月份的第一天
     */
    public static String getFirstDateOfMonth(int year, int month, String format) {
        Calendar cal = getCalendar(year, month);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return getTime(cal.getTime(), format);
    }

    /**
     * 获得指定月份的最后一天
     */
    public static String getLastDateOfMonth(int year, int month, String format) {
        Calendar cal = getCalendar(year, month);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return getTime(cal.getTime(), format);
    }

    /**
     * 将日期字符串，转换成根据指定日期格式.
     * 如转换失败，返回原字符串
     */
    public static String convertFormat(String sTime, String pFormat, String nFormat) {
        if (null == sTime || sTime.trim().length() <= 0) return "";
        long timeStamp = getTimeStamp(sTime, pFormat);
        return -1 != timeStamp ? getTime(timeStamp, nFormat) : sTime;
    }

    /**
     * 获得指定年月的日历
     */
    @SuppressWarnings("MagicConstant")
    private static Calendar getCalendar(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal;
    }

    /**
     * 根据时间戳与日期偏移值返回指定格式的时间
     */
    private static String getTime(long timeStamp, int delta, DateFormat dateFormat) {
        return dateFormat.format(new Date(timeStamp + delta * DAY));
    }
}
