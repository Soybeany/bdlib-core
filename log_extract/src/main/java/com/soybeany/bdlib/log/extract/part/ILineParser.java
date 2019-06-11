package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.parser.IParser;

/**
 * 行内容解析器
 * <br>Created by Soybeany on 2019/6/6.
 */
public interface ILineParser<Row extends IRow, Item extends IItem<Row>> extends IParser<String, Item> {

    @Override
    default Item toOutput(String s) {
        throw new RuntimeException("不支持此方法");
    }

    /**
     * 解析行内容，并返回已完成的item
     *
     * @return 可null，若为非null，则表示解析出完整的Item
     */
    Item parseLineAndGetFinishedItem(ItemData<Row, Item> data, String line);

}
