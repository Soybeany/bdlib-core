package com.soybeany.bdlib.log.extract.std.model;

import java.text.DateFormat;

/**
 * 请求的Item
 * <br>Created by Soybeany on 2019/6/3.
 */
public class StdRequestItem extends StdItem {

    public String method;

    public String url;

    public String param;

    public StdRequestItem(DateFormat format) {
        super(format);
    }

}
