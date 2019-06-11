package com.soybeany.bdlib.log.extract.std.conductor;

import com.soybeany.bdlib.log.extract.conductor.FlagItemProvider;
import com.soybeany.bdlib.log.extract.conductor.IStateOption;
import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.part.ItemData;
import com.soybeany.bdlib.log.extract.std.model.StdItem;
import com.soybeany.bdlib.log.extract.std.model.StdRow;

public class StdStartOption implements IStateOption<StdRow, StdItem> {
    @Override
    public StdItem onOption(ItemData<StdRow, StdItem> data, StdItem item, StdRow row, IFlag flag, FlagItemProvider<StdRow, ? extends StdItem, ?> provider) {
        // 创建新item并入map
        StdItem newItem = provider.getNewItem(row, flag);
        newItem.startTime = row.dateAndTime;
        data.cacheMap.put(row.thread, newItem);
        // 若已有item，将其移入正式列表
        return item;
    }
}