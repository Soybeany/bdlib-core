package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

/**
 * 字符串过滤器，适用于url、param、method、thread、level等
 * <br>判断指定字符串中是否含有特定字符串
 * <br>Created by Soybeany on 2019/5/31.
 */
public class StringFilter<Item> extends IFilter.Impl<Item, String> {
    private IKeyContainer<String> mContainer;

    public StringFilter(IKeyContainer<String> container, IDataProvider<Item, String> provider) {
        super(provider);
        mContainer = container;
    }

    @Override
    public boolean isActive() {
        return null != mContainer && mContainer.hasKey();
    }

    @Override
    protected boolean shouldInterceptWithData(String target) {
        return mContainer.isTargetContainKey(target);
    }
}
