package com.soybeany.bdlib.log.extract.std;

/**
 * <br>Created by Soybeany on 2019/6/3.
 */
public class StdTimeUtils {

    public static int toHourMinuteIndex(String time) {
        int hour = toInt(time, 0);
        int minute = toInt(time, 3);
        return hour * 60 + minute;
    }

    public static int toInt(String source, int index) {
        return Integer.parseInt(source.substring(index, index + 2));
    }
}
