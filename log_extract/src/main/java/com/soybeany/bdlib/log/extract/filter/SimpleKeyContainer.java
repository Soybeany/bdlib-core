package com.soybeany.bdlib.log.extract.filter;

/**
 * 简单的键容器
 * <br>Created by Soybeany on 2019/6/1.
 */
public class SimpleKeyContainer implements IKeyContainer<String> {
    private String mKey;

    public SimpleKeyContainer(String key) {
        mKey = (null != key ? key.trim().toLowerCase() : null);
    }

    @Override
    public boolean hasKey() {
        return null != mKey && !mKey.isEmpty();
    }

    @Override
    public boolean isTargetContainKey(String target) {
        return target.toLowerCase().contains(mKey);
    }
}
