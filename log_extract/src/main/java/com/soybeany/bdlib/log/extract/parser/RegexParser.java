package com.soybeany.bdlib.log.extract.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则解析器，适用于内容、标签等的解析
 * <br>Created by Soybeany on 2019/6/3.
 */
public class RegexParser<Output> extends IParser.Impl<String, String, Output, Matcher> {

    public RegexParser(String regex, ICallback<Output, Matcher> callback, IOutputProvider<Output> outputProvider, boolean recycleOutput) {
        super(regex, callback, outputProvider, false, recycleOutput);
    }

    @Override
    protected Matcher getData(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (!matcher.find()) {
            return null;
        }
        return matcher;
    }
}
