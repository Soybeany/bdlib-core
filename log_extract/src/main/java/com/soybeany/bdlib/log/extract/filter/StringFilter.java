package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

/**
 * 字符串过滤器，适用于url、param、method、thread、level等
 * <br>判断指定字符串中是否含有特定字符串
 * <br>Created by Soybeany on 2019/5/31.
 */
public class StringFilter<Item> extends IFilter.Impl<Item, String> {
    private String mData;

    public StringFilter(String data, IDataProvider<Item, String> provider) {
        super(provider);
        mData = null != data ? data.trim().toLowerCase() : null;
    }

    @Override
    public boolean isActive() {
        return null != mData && !mData.isEmpty();
    }

    @Override
    protected boolean shouldInterceptWithData(String data) {
        return !containKey(data);
    }

    public boolean containKey(String data) {
        return data.toLowerCase().contains(mData);
    }

    // //////////////////////////////////内部类//////////////////////////////////

    public static class StringItemFilter extends StringFilter<String> {
        public StringItemFilter(String data) {
            super(data, key -> key);
        }
    }
}
