package com.soybeany.bdlib.log.extract.parser;

import com.soybeany.bdlib.log.extract.model.TimeIndex;

/**
 * 解析日期/时间
 * <br>Created by Soybeany on 2019/5/31.
 */
public class TimeParser extends IParser.Impl<Object, String, TimeIndex, String> {

    public TimeParser(ICallback<TimeIndex, String> callback, boolean recycleOutput) {
        super(null, callback, TimeIndex::new, false, recycleOutput);
    }

    @Override
    protected String getData(Object obj, String s) {
        return s;
    }
}
