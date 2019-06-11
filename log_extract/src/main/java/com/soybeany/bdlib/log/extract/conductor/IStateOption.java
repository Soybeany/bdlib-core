package com.soybeany.bdlib.log.extract.conductor;

import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.part.ItemData;

public interface IStateOption<Row extends IRow, Item extends IItem<Row>> {
    /**
     * 实际操作
     *
     * @param data     结果项数据
     * @param item     前一匹配的item
     * @param flag     当前的标识
     * @param provider 新Item的提供者
     * @return 可为null，表示解析出的Item
     */
    Item onOption(ItemData<Row, Item> data, Item item, Row row, IFlag flag, FlagItemProvider<Row, ? extends Item, ?> provider);
}