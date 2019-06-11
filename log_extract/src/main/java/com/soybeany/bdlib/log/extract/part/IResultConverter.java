package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;

import java.util.List;

/**
 * 结果转换器
 * <br>Created by Soybeany on 2019/6/6.
 */
public interface IResultConverter<Row extends IRow, Item extends IItem<Row>, Result> {

    Result toResult(ExtInfo info, List<Item> items);

    /**
     * 额外提供的信息
     */
    class ExtInfo {
        public String msg;
    }
}
