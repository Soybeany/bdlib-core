package com.soybeany.bdlib.log.extract.std.parser;

import com.soybeany.bdlib.log.extract.parser.TimeParser;
import com.soybeany.bdlib.log.extract.std.StdTimeUtils;

/**
 * 标准的时间解析器
 * 19-02-14 08:36:47
 * <br>Created by Soybeany on 2019/6/3.
 */
public class StdTimeParser extends TimeParser {
    public StdTimeParser() {
        super((index, input) -> {
            index.date = input.substring(0, 8);
            index.index = StdTimeUtils.toHourMinuteIndex(input.substring(9));
        }, true);
    }
}
