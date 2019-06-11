package com.soybeany.bdlib.log.extract.std;

import com.google.gson.Gson;
import com.soybeany.bdlib.log.extract.LogExtractUtils;
import com.soybeany.bdlib.log.extract.conductor.FlagItemProvider;
import com.soybeany.bdlib.log.extract.conductor.IStateOption;
import com.soybeany.bdlib.log.extract.conductor.ItemProvider;
import com.soybeany.bdlib.log.extract.conductor.LineParser;
import com.soybeany.bdlib.log.extract.efb.RequestItemProvider;
import com.soybeany.bdlib.log.extract.filter.KeyFilter;
import com.soybeany.bdlib.log.extract.filter.SimpleKeyMatcher;
import com.soybeany.bdlib.log.extract.model.Range;
import com.soybeany.bdlib.log.extract.part.ILineParser;
import com.soybeany.bdlib.log.extract.part.ILineProvider;
import com.soybeany.bdlib.log.extract.part.IResultConverter;
import com.soybeany.bdlib.log.extract.part.ItemData;
import com.soybeany.bdlib.log.extract.std.conductor.StdFinishOption;
import com.soybeany.bdlib.log.extract.std.conductor.StdStartOption;
import com.soybeany.bdlib.log.extract.std.conductor.StdTimerItemProvider;
import com.soybeany.bdlib.log.extract.std.model.StdItem;
import com.soybeany.bdlib.log.extract.std.model.StdNormItem;
import com.soybeany.bdlib.log.extract.std.model.StdRow;
import com.soybeany.bdlib.log.extract.std.parser.StdFlagParser;
import com.soybeany.bdlib.log.extract.std.parser.StdRowParser;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <br>Created by Soybeany on 2019/6/3.
 */
public class SourceParseTest {

    @Test
    public void parse2() {
        LogExtractUtils.QueryPart<StdRow, StdItem, String> query = LogExtractUtils.query(new LineProvider(), getNewLineParser());
        query.filters(Arrays.asList(new KeyFilter.Include<>(new SimpleKeyMatcher("exec-10"), item -> item.thread)));
        String result = query.toResult(new ResultConverter());
        System.out.println("结果:" + result);
    }

    private static class ResultConverter implements IResultConverter<StdRow, StdItem, String> {
        Gson mGson = new Gson();

        @Override
        public String toResult(ExtInfo info, List<StdItem> list) {
            return info.msg + "\nlist:" + mGson.toJson(list);
        }
    }

    private static class LineProvider implements ILineProvider {
        private String[] mSource = TestSource.SOURCE1.split("\\n");
        private int mIndex;

        @Override
        public boolean hasNext() {
            return mIndex < mSource.length;
        }

        @Override
        public String next() {
            return mSource[mIndex++];
        }

        @Override
        public String stateMsg() {
            return hasNext() ? "还有下一行" : "已到达文本末尾";
        }

        @Override
        public void setRange(Range range) {

        }
    }

    @Test
    public void parse() {
        ILineParser<StdRow, StdItem> lineParser = getNewLineParser();
        // 拆分成行
        ItemData<StdRow, StdItem> data = new ItemData<>();
        List<StdItem> result = new LinkedList<>();
        for (String line : TestSource.SOURCE1.split("\\n")) {
            StdItem item = lineParser.parseLineAndGetFinishedItem(data, line);
            if (null != item) {
                result.add(item);
            }
        }

        System.out.println("item数目:" + result.size() + " 缓存数:" + data.cacheMap.size() + " 忽略数:" + data.ignoreLines);
    }


    private ILineParser<StdRow, StdItem> getNewLineParser() {
        Map<String, FlagItemProvider<StdRow, ? extends StdItem, ?>> consumerMap = new HashMap<>();
        Map<String, IStateOption<StdRow, StdItem>> optionMap = new HashMap<>();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        RequestItemProvider requestConsumer = new RequestItemProvider(format);
        consumerMap.put("客户端", requestConsumer);
        consumerMap.put("管理端", requestConsumer);
        consumerMap.put("定时器", new StdTimerItemProvider(format));

        optionMap.put("开始", new StdStartOption());
        optionMap.put("结束", new StdFinishOption());

        return new LineParser<>(
                consumerMap, optionMap, new ItemProvider<StdRow, StdItem>() {
            @Override
            public StdItem getNewItem(StdRow row) {
                return new StdNormItem(format);
            }
        }, new StdRowParser(), new StdFlagParser());
    }

}
