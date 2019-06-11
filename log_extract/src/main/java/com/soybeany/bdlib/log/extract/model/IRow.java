package com.soybeany.bdlib.log.extract.model;

/**
 * <br>Created by Soybeany on 2019/6/5.
 */
public interface IRow {

    /**
     * 获得分组id(将row进行分组的依据，常使用线程名)
     */
    String getGroupId();

    /**
     * 获得日志
     */
    CharSequence getLog();

    /**
     * 设置日志
     */
    void setLog(CharSequence log);
}
