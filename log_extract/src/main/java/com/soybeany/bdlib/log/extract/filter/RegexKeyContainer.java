package com.soybeany.bdlib.log.extract.filter;

import java.util.regex.Pattern;

/**
 * 正则键容器
 * <br>Created by Soybeany on 2019/6/1.
 */
public class RegexKeyContainer implements IKeyContainer<String> {
    private int mFlags;
    private String mRegex;

    public RegexKeyContainer(String regex) {
        mRegex = (null != regex ? regex.trim() : null);
    }

    @Override
    public boolean hasKey() {
        return null != mRegex && !mRegex.isEmpty();
    }

    @Override
    public boolean isTargetContainKey(String target) {
        return Pattern.compile(mRegex, mFlags).matcher(target).find();
    }

    /**
     * 设置{@link Pattern#compile(String, int)}中的flags
     * <br>可填入如{@link Pattern#CASE_INSENSITIVE}忽略大小写
     */
    public RegexKeyContainer patternFlag(int flags) {
        mFlags = flags;
        return this;
    }
}
