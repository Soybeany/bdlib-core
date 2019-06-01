package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.filter.StringFilter.StringItemFilter;
import com.soybeany.bdlib.log.extract.model.IDataProvider;

import java.util.Iterator;
import java.util.List;

/**
 * 简单文本过滤
 * <br>Created by Soybeany on 2019/5/31.
 */
public abstract class SimpleTextFilter<Item> extends IFilter.Impl<Item, List<String>> {
    private StringItemFilter mFilter;

    public SimpleTextFilter(String key, IDataProvider<Item, List<String>> provider) {
        super(provider);
        mFilter = new StringItemFilter(key);
    }

    @Override
    public boolean isActive() {
        return null != mFilter && mFilter.isActive();
    }

    @Override
    protected boolean shouldInterceptWithData(List<String> list) {
        return shouldInterceptWithData(mFilter, list);
    }

    protected abstract boolean shouldInterceptWithData(StringItemFilter filter, List<String> strings);

    /**
     * 关键字包含
     */
    public static class Include<Item> extends SimpleTextFilter<Item> {
        private boolean mOnlyKeepIncludeRow; // 是否只保留includeKey的条目

        public Include(String key, IDataProvider<Item, List<String>> provider) {
            super(key, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(StringItemFilter filter, List<String> list) {
            Iterator<String> iterator = list.iterator();
            boolean containKey = false;
            while (iterator.hasNext()) {
                String input = iterator.next();
                if (filter.containKey(input)) {
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
        public Include<Item> onlyKeepInclude(boolean flag) {
            mOnlyKeepIncludeRow = flag;
            return this;
        }
    }

    /**
     * 关键字排除
     */
    public static class Exclude<Item> extends SimpleTextFilter<Item> {
        private boolean mOnlyRemoveExcludeRow; // 是否只移除excludeKey的条目

        public Exclude(String key, IDataProvider<Item, List<String>> provider) {
            super(key, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(StringItemFilter filter, List<String> list) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String input = iterator.next();
                if (!filter.containKey(input)) {
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
        public Exclude<Item> onlyRemoveExclude(boolean flag) {
            mOnlyRemoveExcludeRow = flag;
            return this;
        }
    }
}
