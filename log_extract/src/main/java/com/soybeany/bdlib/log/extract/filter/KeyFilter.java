package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.model.IRow;
import com.soybeany.bdlib.log.extract.parser.IParser;

import java.util.Iterator;
import java.util.List;

/**
 * 关键词过滤器
 * <br>默认String实现类适用于url、param、method、thread、level等
 * <br>List的String实现类适用于log等
 * <br>Created by Soybeany on 2019/5/31.
 */
public abstract class KeyFilter<Row extends IRow, Item extends IItem<Row>, Data> extends IFilter.Impl<Item, Data> {
    private IKeyMatcher mKeyMatcher;

    public KeyFilter(IKeyMatcher keyMatcher, IParser<Item, Data> provider) {
        super(provider);
        mKeyMatcher = keyMatcher;
    }

    @Override
    public boolean isActive() {
        return null != mKeyMatcher && mKeyMatcher.hasKey();
    }

    @Override
    protected boolean shouldInterceptWithData(Data data) {
        return shouldInterceptWithData(mKeyMatcher, data);
    }

    protected abstract boolean shouldInterceptWithData(IKeyMatcher keyMatcher, Data data);

    // //////////////////////////////////默认实现类//////////////////////////////////

    /**
     * 关键字包含
     */
    public static class Include<Row extends IRow, Item extends IItem<Row>> extends KeyFilter<Row, Item, String> {
        public Include(IKeyMatcher keyMatcher, IParser<Item, String> provider) {
            super(keyMatcher, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyMatcher keyMatcher, String key) {
            return !keyMatcher.isTargetContainKey(key);
        }
    }

    /**
     * 关键字排除
     */
    public static class Exclude<Row extends IRow, Item extends IItem<Row>> extends KeyFilter<Row, Item, String> {
        public Exclude(IKeyMatcher keyMatcher, IParser<Item, String> provider) {
            super(keyMatcher, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyMatcher keyMatcher, String key) {
            return keyMatcher.isTargetContainKey(key);
        }
    }

    // //////////////////////////////////List实现类//////////////////////////////////

    /**
     * List关键字包含
     */
    public static class ListInclude<Row extends IRow, Item extends IItem<Row>> extends KeyFilter<Row, Item, List<String>> {
        private boolean mOnlyKeepIncludeRow; // 是否只保留includeKey的条目

        public ListInclude(IKeyMatcher keyMatcher, IParser<Item, List<String>> provider) {
            super(keyMatcher, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyMatcher container, List<String> list) {
            Iterator<String> iterator = list.iterator();
            boolean containKey = false;
            while (iterator.hasNext()) {
                String input = iterator.next();
                if (container.isTargetContainKey(input)) {
                    containKey = true;
                } else if (mOnlyKeepIncludeRow) { // 若只保留包含key的条目，则移除
                    iterator.remove();
                }
            }
            return !containKey;
        }

        /**
         * 是否只保留includeKey的条目
         */
        public ListInclude<Row, Item> onlyKeepInclude(boolean flag) {
            mOnlyKeepIncludeRow = flag;
            return this;
        }
    }

    /**
     * List关键字排除
     */
    public static class ListExclude<Row extends IRow, Item extends IItem<Row>> extends KeyFilter<Row, Item, List<String>> {
        private boolean mOnlyRemoveExcludeRow; // 是否只移除excludeKey的条目

        public ListExclude(IKeyMatcher container, IParser<Item, List<String>> provider) {
            super(container, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyMatcher container, List<String> list) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String input = iterator.next();
                if (!container.isTargetContainKey(input)) {
                    continue;
                }
                if (!mOnlyRemoveExcludeRow) {
                    return true;
                }
                iterator.remove();
            }
            return list.isEmpty();
        }

        /**
         * 是否只移除excludeKey的条目
         */
        public ListExclude<Row, Item> onlyRemoveExclude(boolean flag) {
            mOnlyRemoveExcludeRow = flag;
            return this;
        }
    }
}
