package com.soybeany.bdlib.log.extract.std.conductor;

import com.soybeany.bdlib.log.extract.parser.IParser;
import com.soybeany.bdlib.log.extract.std.model.StdTimerItem;

import java.text.DateFormat;

/**
 * <br>Created by Soybeany on 2019/6/4.
 */
public class StdTimerItemProvider extends StdFlagItemProvider<StdTimerItem, StdTimerItemProvider.TimerDetail> {
    public StdTimerItemProvider(DateFormat format) {
        super(StdTimerItem.class, new DetailParser(), format);
    }

    @Override
    protected StdTimerItem getNewItem(DateFormat dateFormat, TimerDetail detail) {
        StdTimerItem item = new StdTimerItem(dateFormat);
        item.desc = detail.desc;
        return item;
    }

    static class DetailParser extends IParser.Impl<String, String, TimerDetail, String> {
        DetailParser() {
            super(null, (detail, data) -> {
                detail.desc = data;
            }, TimerDetail::new, false, true);
        }

        @Override
        protected String getData(String param, String input) {
            return input;
        }
    }

    static class TimerDetail {
        String desc;
    }
}
