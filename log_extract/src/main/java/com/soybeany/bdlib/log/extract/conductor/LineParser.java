package com.soybeany.bdlib.log.extract.conductor;

import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.parser.IParser;
import com.soybeany.bdlib.log.extract.part.ILineParser;
import com.soybeany.bdlib.log.extract.part.ItemData;

import java.util.Map;

/**
 * 行内容解析者
 * <br>Created by Soybeany on 2019/6/5.
 */
public class LineParser<Row extends IRow, Item extends IItem<Row>, Flag extends IFlag> implements ILineParser<Row, Item> {
    private final Map<String, FlagItemProvider<Row, ? extends Item, ?>> mFlagProviderMap;
    private final Map<String, IStateOption<Row, Item>> mOptionMap;
    private final ItemProvider<Row, Item> mItemProvider;

    private final IParser<String, Row> mRowParser;
    private final IParser<String, Flag> mFlagParser;


    public LineParser(Map<String, FlagItemProvider<Row, ? extends Item, ?>> flagProviderMap,
                      Map<String, IStateOption<Row, Item>> optionMap,
                      ItemProvider<Row, Item> itemProvider,
                      IParser<String, Row> rowParser, IParser<String, Flag> flagParser) {
        mFlagProviderMap = flagProviderMap;
        mOptionMap = optionMap;
        mItemProvider = itemProvider;
        mRowParser = rowParser;
        mFlagParser = flagParser;
    }

    @Override
    public Item parseLineAndGetFinishedItem(ItemData<Row, Item> data, String line) {
        Row curRow = mRowParser.toOutput(line);
        // 拼接上一行
        if (null == curRow) {
            if (null == data.lastRow) {
                data.ignoreLines++;
                return null;
            }
            CharSequence log = data.lastRow.getLog();
            if (!(log instanceof StringBuilder)) {
                data.lastRow.setLog(log = new StringBuilder(log));
            }
            ((StringBuilder) log).append("\n").append(line);
            return null;
        }
        data.lastRow = curRow;
        String groupId = curRow.getGroupId();
        // 解析Flag
        Flag flag = mFlagParser.toOutput(curRow.getLog().toString());
        // 若为标签
        if (null != flag) {
            FlagItemProvider<Row, ? extends Item, ?> flagItemProvider = mFlagProviderMap.get(flag.getType());
            Item item = data.cacheMap.remove(groupId);
            return mOptionMap.get(flag.getState()).onOption(data, item, curRow, flag, flagItemProvider);
        }
        // 获得或创建item
        Item item = data.cacheMap.get(groupId);
        if (null == item) {
            data.cacheMap.put(groupId, item = mItemProvider.getNewItem(curRow));
        }
        item.getLogs().add(curRow);
        return null;
    }
}
