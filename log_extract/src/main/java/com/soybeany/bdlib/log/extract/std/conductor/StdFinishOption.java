package com.soybeany.bdlib.log.extract.std.conductor;

import com.soybeany.bdlib.log.extract.conductor.FlagItemProvider;
import com.soybeany.bdlib.log.extract.conductor.IStateOption;
import com.soybeany.bdlib.log.extract.model.IFlag;
import com.soybeany.bdlib.log.extract.part.ItemData;
import com.soybeany.bdlib.log.extract.std.model.StdItem;
import com.soybeany.bdlib.log.extract.std.model.StdRow;

public class StdFinishOption implements IStateOption<StdRow, StdItem> {
    @Override
    public StdItem onOption(ItemData<StdRow, StdItem> data, StdItem item, StdRow row, IFlag flag, FlagItemProvider<StdRow, ? extends StdItem, ?> provider) {
        if (null != item) {
            // 若已有item，进行值覆盖，以免类型不同
            item = provider.cover(row, flag, item);
        } else {
            // 若无，创建新item
            item = provider.getNewItem(row, flag);
        }
        item.finishTime = row.dateAndTime;
        return item;
    }
}