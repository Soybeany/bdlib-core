package com.soybeany.bdlib.log.extract.std.model;

import com.soybeany.bdlib.log.extract.model.IRow;

/**
 * <br>Created by Soybeany on 2019/6/2.
 */
public class StdRow implements IRow {

    /**
     * 日期与时间 [19-02-14 08:36:47]
     */
    public String dateAndTime;

    /**
     * 等级 [INFO/WARN/ERROR]等
     */
    public String level;

    /**
     * 线程 [http-nio-8080-exec-1]
     */
    public String thread;

    /**
     * 输出日志的类及行数 {AuthController:26}
     */
    public String classAndLineNum;

    /**
     * 日志内容 请求(7060001b)开始
     */
    public CharSequence log;

    @Override
    public String toString() {
        return dateAndTime + " " + level + " " + log + " (" + classAndLineNum + ")";
    }

    @Override
    public String getGroupId() {
        return thread;
    }

    @Override
    public CharSequence getLog() {
        return log;
    }

    @Override
    public void setLog(CharSequence log) {
        this.log = log;
    }
}
