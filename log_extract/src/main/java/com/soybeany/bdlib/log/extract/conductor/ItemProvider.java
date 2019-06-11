package com.soybeany.bdlib.log.extract.conductor;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;

/**
 * 普通Item提供者
 * <br>Created by Soybeany on 2019/6/4.
 */
public abstract class ItemProvider<Row extends IRow, Item extends IItem<Row>> {
    public abstract Item getNewItem(Row row);
}
