package com.soybeany.bdlib.log.extract.std.parser;

import com.soybeany.bdlib.log.extract.parser.RegexParser;
import com.soybeany.bdlib.log.extract.std.model.StdFlag;

/**
 * 标准标签解析器
 * <br>Created by Soybeany on 2019/6/2.
 */
public class StdFlagParser extends RegexParser<StdFlag> {
    private static final String EX = "^FLAG-(.+?)-(.+?):(.*)";

    public StdFlagParser() {
        super(EX, (flag, matcher) -> {
            flag.type = matcher.group(1);
            flag.state = matcher.group(2);
            flag.detail = matcher.group(3);
        }, StdFlag::new, true);
    }
}
