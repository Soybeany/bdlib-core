package com.soybeany.bdlib.log.extract.std.conductor;

import com.soybeany.bdlib.log.extract.conductor.FlagItemProvider;
import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.parser.IParser;
import com.soybeany.bdlib.log.extract.std.model.StdItem;
import com.soybeany.bdlib.log.extract.std.model.StdRow;

import java.text.DateFormat;

/**
 * <br>Created by Soybeany on 2019/6/4.
 */
public abstract class StdFlagItemProvider<Item extends StdItem, FlagDetail> extends FlagItemProvider<StdRow, Item, FlagDetail> {
    private DateFormat mDateFormat;

    protected StdFlagItemProvider(Class<Item> clazz, IParser<String, FlagDetail> detailParser, DateFormat format) {
        super(clazz, detailParser);
        mDateFormat = format;
    }

    @Override
    public Item getNewItem(StdRow row, IFlag flag, FlagDetail detail) {
        Item item = getNewItem(mDateFormat, detail);
        item.thread = row.thread;
        return item;
    }

    protected abstract Item getNewItem(DateFormat dateFormat, FlagDetail detail);
}
