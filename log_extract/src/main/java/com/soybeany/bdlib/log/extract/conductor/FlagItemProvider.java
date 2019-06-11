package com.soybeany.bdlib.log.extract.conductor;

import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.parser.IParser;

/**
 * 标签Item提供者
 * <br>Created by Soybeany on 2019/6/4.
 */
public abstract class FlagItemProvider<Row extends IRow, Item extends IItem<Row>, FlagDetail> {
    private final Class<Item> mClazz;
    private final IParser<String, FlagDetail> mDetailParser;

    public FlagItemProvider(Class<Item> clazz, IParser<String, FlagDetail> detailParser) {
        mClazz = clazz;
        mDetailParser = detailParser;
    }

    public Item getNewItem(Row row, IFlag flag) {
        return getNewItem(row, flag, mDetailParser.toOutput(flag.getDetail()));
    }

    /**
     * 尝试进行item覆盖
     */
    @SuppressWarnings("unchecked")
    public Item cover(Row row, IFlag flag, IItem<Row> item) {
        if (mClazz.equals(item.getClass())) {
            return (Item) item;
        }
        Item result = getNewItem(row, flag);
        result.cover(item);
        return result;
    }

    protected abstract Item getNewItem(Row row, IFlag flag, FlagDetail detail);
}
