package com.soybeany.bdlib.log.extract.std.conductor;

import com.soybeany.bdlib.log.extract.parser.RegexParser;
import com.soybeany.bdlib.log.extract.std.model.StdRequestItem;

import java.text.DateFormat;

/**
 * <br>Created by Soybeany on 2019/6/4.
 */
public class StdRequestItemProvider extends StdFlagItemProvider<StdRequestItem, StdRequestItemProvider.Detail> {

    public StdRequestItemProvider(DateFormat format) {
        super(StdRequestItem.class, new DetailParser(), format);
    }

    @Override
    protected StdRequestItem getNewItem(DateFormat dateFormat, Detail detail) {
        StdRequestItem item = new StdRequestItem(dateFormat);
        item.method = detail.method;
        item.url = detail.url;
        item.param = detail.param;
        return item;
    }

    /**
     * GET /xxx/xx/xx {param=yyy}
     * <br>Created by Soybeany on 2019/6/3.
     */
    static class DetailParser extends RegexParser<Detail> {
        private static final String EX = "(.+?) (.+?) \\{(.*)}";

        DetailParser() {
            super(EX, (detail, matcher) -> {
                detail.method = matcher.group(1);
                detail.url = matcher.group(2);
                detail.param = matcher.group(3);
            }, Detail::new, true);
        }
    }

    static class Detail {
        String method;
        String url;
        String param;
    }
}
