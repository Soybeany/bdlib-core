package com.soybeany.bdlib.log.extract.std.model;

import java.text.DateFormat;

/**
 * 定时器的Item
 * <br>Created by Soybeany on 2019/6/5.
 */
public class StdTimerItem extends StdItem {

    public String desc;

    public StdTimerItem(DateFormat format) {
        super(format);
    }
}
