package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

/**
 * <br>Created by Soybeany on 2019/5/31.
 */
public interface IFilter<Item> {
    /**
     * 是否激活状态，即此过滤器是否要工作
     */
    boolean isActive();

    /**
     * 是否进行拦截操作
     *
     * @return true 表示进行拦截
     */
    boolean shouldIntercept(Item item);

    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    abstract class Impl<Item, Data> implements IFilter<Item> {
        private final IDataProvider<Item, Data> mProvider;
        private boolean mInterceptWhenItemIsNull;

        public Impl(IDataProvider<Item, Data> provider) {
            mProvider = provider;
        }

        @Override
        public boolean shouldIntercept(Item item) {
            return null == item ? mInterceptWhenItemIsNull : shouldInterceptWithData(mProvider.getData(item));
        }

        /**
         * 当输入的item为null时，是否进行拦截，即{@link #shouldIntercept(Item)}返回true
         * <br>默认为false
         */
        public Impl<Item, Data> interceptWhenItemIsNull(boolean flag) {
            mInterceptWhenItemIsNull = flag;
            return this;
        }

        /**
         * 对于{@link Data}是否需要拦截
         *
         * @return true 表示进行拦截
         */
        protected abstract boolean shouldInterceptWithData(Data data);
    }
}
