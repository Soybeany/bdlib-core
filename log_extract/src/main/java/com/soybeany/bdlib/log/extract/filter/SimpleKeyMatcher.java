package com.soybeany.bdlib.log.extract.filter;

/**
 * 简单的匹配器
 * <br>Created by Soybeany on 2019/6/1.
 */
public class SimpleKeyMatcher implements IKeyMatcher {
    private String mKey;

    public SimpleKeyMatcher(String key) {
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
