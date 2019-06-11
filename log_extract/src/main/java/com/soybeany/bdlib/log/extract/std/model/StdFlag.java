package com.soybeany.bdlib.log.extract.std.model;

import com.soybeany.bdlib.log.extract.model.IFlag;

/**
 * 标准标签
 * <br>Created by Soybeany on 2019/6/3.
 */
public class StdFlag implements IFlag {

    /**
     * 类型(客户端、管理端、定时器等)
     */
    public String type;

    /**
     * 状态(开始、结束等)
     */
    public String state;

    /**
     * 详情
     */
    public String detail;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
