package com.soybeany.bdlib.log.extract.std.model;

import com.soybeany.bdlib.log.extract.model.IItem;

import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * 普通Item
 * <br>Created by Soybeany on 2019/6/3.
 */
public abstract class StdItem implements IItem<StdRow> {

    /**
     * 开始时间
     */
    public String startTime;

    /**
     * 结束时间
     */
    public String finishTime;

    /**
     * 线程
     */
    public String thread;

    /**
     * 日志
     */
    private final List<StdRow> rows = new LinkedList<>();

    private transient DateFormat mTimeFormat;

    public StdItem(DateFormat format) {
        mTimeFormat = format;
    }

    @Override
    public boolean cover(IItem<StdRow> item) {
        if (!(item instanceof StdItem)) {
            return false;
        }
        StdItem stdItem = (StdItem) item;
        startTime = stdItem.startTime;
        finishTime = stdItem.finishTime;
        thread = stdItem.thread;
        rows.addAll(stdItem.rows);
        mTimeFormat = stdItem.mTimeFormat;
        return true;
    }

    @Override
    public List<StdRow> getLogs() {
        return rows;
    }

    /**
     * 耗时
     */
    public String getSpend() {
        try {
            long start = mTimeFormat.parse(startTime).getTime();
            long finish = mTimeFormat.parse(finishTime).getTime();
            return (finish - start) / 1000 + "s";
        } catch (Exception e) {
            return "未知";
        }
    }
}
