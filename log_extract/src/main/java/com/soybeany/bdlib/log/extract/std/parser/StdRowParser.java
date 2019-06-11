package com.soybeany.bdlib.log.extract.std.parser;

import com.soybeany.bdlib.log.extract.parser.RegexParser;
import com.soybeany.bdlib.log.extract.std.model.StdRow;

/**
 * 标准行解析器
 * <br> [date time] [level] [thread] {className:classLine}-log
 * <br>[19-02-14 08:36:47] [INFO] [http-nio-8080-exec-1] {AuthController:26}-成功
 * <br>Created by Soybeany on 2019/6/2.
 */
public class StdRowParser extends RegexParser<StdRow> {
    private static final String EX = "\\[(.{17})] \\[(.{4,5})] \\[(.+?)] \\{(.+?)}-(.+)";

    public StdRowParser() {
        super(EX, (row, matcher) -> {
            row.dateAndTime = matcher.group(1);
            row.level = matcher.group(2);
            row.thread = matcher.group(3);
            row.classAndLineNum = matcher.group(4);
            row.log = matcher.group(5);
        }, StdRow::new, false);
    }
}
