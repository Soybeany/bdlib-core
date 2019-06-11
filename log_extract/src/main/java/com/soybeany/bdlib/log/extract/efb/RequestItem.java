package com.soybeany.bdlib.log.extract.efb;

import com.soybeany.bdlib.log.extract.std.model.StdItem;

import java.text.DateFormat;

/**
 * <br>Created by Soybeany on 2019/6/3.
 */
public class RequestItem extends StdItem {

    public String user;

    public String url;

    public String param;

    public RequestItem(DateFormat format) {
        super(format);
    }

}
