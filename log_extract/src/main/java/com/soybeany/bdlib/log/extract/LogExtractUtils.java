package com.soybeany.bdlib.log.extract;

import com.soybeany.bdlib.log.extract.filter.IFilter;
import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.part.IItemSorter;
import com.soybeany.bdlib.log.extract.part.ILineParser;
import com.soybeany.bdlib.log.extract.part.ILineProvider;
import com.soybeany.bdlib.log.extract.part.IResultConverter;
import com.soybeany.bdlib.log.extract.part.ItemData;

import java.util.LinkedList;
import java.util.List;

/**
 * 日志提取入口
 * <br>Created by Soybeany on 2019/5/31.
 */
public class LogExtractUtils {

    public static void scan() {

    }

    public static <Row extends IRow, Item extends IItem<Row>, Result> QueryPart<Row, Item, Result>
    query(ILineProvider provider, ILineParser<Row, Item> parser) {
        return new QueryPart<>(provider, parser);
    }

    public static class QueryPart<Row extends IRow, Item extends IItem<Row>, Result> {

        private final ILineProvider mLineProvider;
        private final ILineParser<Row, Item> mLineParser;

        private boolean mScanFirst;

        private List<IFilter<Item>> mFilters;
        private List<IItemSorter<Item>> mSorters;


        private List<Item> mResults; // 指定结果集
        private ItemData<Row, Item> mItemData;

        private int mMaxNewResult = 30; // 最大新结果数

        QueryPart(ILineProvider provider, ILineParser<Row, Item> parser) {
            if (null == (mLineProvider = provider)) {
                throw new RuntimeException("LineProvider不能为null");
            }
            if (null == (mLineParser = parser)) {
                throw new RuntimeException("LineParser不能为null");
            }
        }

        /**
         * 查询前是否先进行扫描
         */
        public QueryPart<Row, Item, Result> scanFirst(boolean flag) {
            mScanFirst = flag;
            return this;
        }

        /**
         * 最大新结果数
         */
        public QueryPart<Row, Item, Result> maxNewResult(int count) {
            if (count > 0) {
                mMaxNewResult = count;
            }
            return this;
        }

        /**
         * 指定要使用的结果集，对结果进行二次筛选或排序
         */
        public QueryPart<Row, Item, Result> results(List<Item> results) {
            if (null != results) {
                mResults = new LinkedList<>(results);
            }
            return this;
        }

        /**
         * 设置缓存数据，用于断点续查
         */
        public QueryPart<Row, Item, Result> itemData(ItemData<Row, Item> data) {
            mItemData = data;
            return this;
        }

        /**
         * 设置拦截器，进行数据的过滤
         */
        public QueryPart<Row, Item, Result> filters(List<IFilter<Item>> filters) {
            if (null != filters) {
                mFilters = new LinkedList<>();
                for (IFilter<Item> filter : filters) {
                    if (filter.isActive()) {
                        mFilters.add(filter);
                    }
                }
            }
            return this;
        }

        /**
         * 设置排序器，用于数据排序
         */
        public QueryPart<Row, Item, Result> sorters(List<IItemSorter<Item>> sorters) {
            mSorters = sorters;
            return this;
        }

        /**
         * 转换为指定结果，格式转换
         */
        public Result toResult(IResultConverter<Row, Item, Result> converter) {
            if (null == converter) {
                throw new RuntimeException("转换器不能为null");
            }
            IResultConverter.ExtInfo info = new IResultConverter.ExtInfo();
            // 获得结果集
            List<Item> results = (null != mResults ? getFilteredResult(mResults) : getNewResult(info));
            // 排序
            if (null != mSorters) {
                for (IItemSorter<Item> sorter : mSorters) {
                    sorter.onSort(results);
                }
            }
            // 转换
            return converter.toResult(info, results);
        }

        /**
         * item是否被过滤
         */
        private boolean isItemBeFiltered(Item item) {
            if (null == mFilters) {
                return false;
            }
            for (IFilter<Item> filter : mFilters) {
                if (filter.shouldIntercept(item)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 获得过滤后的结果
         */
        private List<Item> getFilteredResult(List<Item> input) {
            List<Item> result = new LinkedList<>();
            for (Item item : input) {
                if (isItemBeFiltered(item)) {
                    continue;
                }
                result.add(item);
            }
            return result;
        }

        /**
         * 查询新结果
         */
        private List<Item> getNewResult(IResultConverter.ExtInfo info) {
            ItemData<Row, Item> itemData = (null != mItemData ? mItemData : new ItemData<>());

            LinkedList<Item> result = new LinkedList<>();
            while (mLineProvider.hasNext()) {
                Item item = mLineParser.parseLineAndGetFinishedItem(itemData, mLineProvider.next());
                if (null == item || isItemBeFiltered(item)) {
                    continue;
                }
                result.add(item);
                if (result.size() >= mMaxNewResult) {
                    info.msg = "已获得指定结果条数";
                    return result;
                }
            }
            // for循环遍历完毕
            info.msg = mLineProvider.stateMsg();
            return result;
        }
    }

}