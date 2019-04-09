package com.soybeany.bdlib.core.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>Created by Soybeany on 2019/2/2.
 */
public class AsyncKeyOptionHelper {

    private static AsyncKeyOptionHelper INSTANCE = getNew();

    private ConcurrentHashMap<String, String> mKeyMap = new ConcurrentHashMap<>();

    public static AsyncKeyOptionHelper getGlobal() {
        return INSTANCE;
    }

    public static AsyncKeyOptionHelper getNew() {
        return new AsyncKeyOptionHelper();
    }

    private AsyncKeyOptionHelper() {

    }

    public String getAndSaveSyncKey(String key) {
        mKeyMap.putIfAbsent(key, key);
        return mKeyMap.get(key);
    }

    public void removeSyncKey(String key) {
        mKeyMap.remove(key);
    }

}
