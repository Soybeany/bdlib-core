package com.soybeany.bdlib.log.extract.efb;

import com.soybeany.bdlib.log.extract.parser.RegexParser;
import com.soybeany.bdlib.log.extract.std.conductor.StdFlagItemProvider;

import java.text.DateFormat;

/**
 * <br>Created by Soybeany on 2019/6/4.
 */
public class RequestItemProvider extends StdFlagItemProvider<RequestItem, RequestItemProvider.Detail> {

    public RequestItemProvider(DateFormat format) {
        super(RequestItem.class, new DetailParser(), format);
    }

    @Override
    protected RequestItem getNewItem(DateFormat dateFormat, Detail detail) {
        RequestItem item = new RequestItem(dateFormat);
        item.url = detail.url;
        item.user = detail.user;
        item.param = detail.param;
        return item;
    }

    /**
     * 123456 /efb/xxx/xx/xx.do {param=yy}
     * <br>Created by Soybeany on 2019/6/3.
     */
    static class DetailParser extends RegexParser<Detail> {
        private static final String EX = "(.+?) (.+?) \\{(.*)}";

        DetailParser() {
            super(EX, (detail, matcher) -> {
                detail.user = matcher.group(1);
                detail.url = matcher.group(2);
                detail.param = matcher.group(3);
            }, Detail::new, true);
        }
    }

    static class Detail {
        String url;
        String param;
        String user;
    }
}
