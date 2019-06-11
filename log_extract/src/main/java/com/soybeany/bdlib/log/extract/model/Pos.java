package com.soybeany.bdlib.log.extract.model;

/**
 * 文件中的位置
 * <br>Created by Soybeany on 2019/5/31.
 */
public class Pos {
    public static final int START_ROW_NUM = 1;

    /**
     * 位点
     */
    public long pointer;

    /**
     * 行号
     */
    public int rowNum = START_ROW_NUM;
}
