package com.soybeany.bdlib.log.extract.model;

import java.util.List;

/**
 * <br>Created by Soybeany on 2019/6/4.
 */
public interface IItem<Row extends IRow> {

    /**
     * 用指定item覆盖当前item部分字段的值
     *
     * @return 是否覆盖成功
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean cover(IItem<Row> iItem);

    /**
     * 获得日志列表
     */
    List<Row> getLogs();
}
