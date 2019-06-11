package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.model.Pos;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果项数据
 * <br>Created by Soybeany on 2019/6/6.
 */
public class ItemData<Row extends IRow, Item extends IItem<Row>> {

    // //////////////////////////////////内容提取器用//////////////////////////////////

    /**
     * 当前已读取的数据位置
     */
    public final Pos pos = new Pos();

    // //////////////////////////////////解析器用//////////////////////////////////

    /**
     * 缓存未完成Item的map
     */
    public final Map<String, Item> cacheMap = new HashMap<>();

    /**
     * 上一解析成功的行
     */
    public IRow lastRow;

    /**
     * 被忽略的行数(信息缺失而无法归类)
     */
    public int ignoreLines;

}
