package com.soybeany.bdlib.log.extract.filter;

/**
 * Key匹配器
 * <br>Created by Soybeany on 2019/6/1.
 */
public interface IKeyMatcher {
    /**
     * 容器中是否含有key
     */
    boolean hasKey();

    /**
     * 指定目标中是否包含有key
     */
    boolean isTargetContainKey(String target);
}
