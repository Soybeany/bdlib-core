package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

import java.util.Iterator;
import java.util.List;

/**
 * 日志过滤
 * <br>Created by Soybeany on 2019/5/31.
 */
public abstract class LogFilter<Item> extends IFilter.Impl<Item, List<String>> {
    private IKeyContainer<String> mContainer;

    public LogFilter(IKeyContainer<String> container, IDataProvider<Item, List<String>> provider) {
        super(provider);
        mContainer = container;
    }

    @Override
    public boolean isActive() {
        return null != mContainer && mContainer.hasKey();
    }

    @Override
    protected boolean shouldInterceptWithData(List<String> list) {
        return shouldInterceptWithData(mContainer, list);
    }

    protected abstract boolean shouldInterceptWithData(IKeyContainer<String> container, List<String> strings);

    // //////////////////////////////////默认实现//////////////////////////////////

    /**
     * 关键字包含
     */
    public static class Include<Item> extends LogFilter<Item> {
        private boolean mOnlyKeepIncludeRow; // 是否只保留includeKey的条目

        public Include(IKeyContainer<String> container, IDataProvider<Item, List<String>> provider) {
            super(container, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyContainer<String> container, List<String> list) {
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
        public Include<Item> onlyKeepInclude(boolean flag) {
            mOnlyKeepIncludeRow = flag;
            return this;
        }
    }

    /**
     * 关键字排除
     */
    public static class Exclude<Item> extends LogFilter<Item> {
        private boolean mOnlyRemoveExcludeRow; // 是否只移除excludeKey的条目

        public Exclude(IKeyContainer<String> container, IDataProvider<Item, List<String>> provider) {
            super(container, provider);
        }

        @Override
        protected boolean shouldInterceptWithData(IKeyContainer<String> container, List<String> list) {
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
        public Exclude<Item> onlyRemoveExclude(boolean flag) {
            mOnlyRemoveExcludeRow = flag;
            return this;
        }
    }
}
