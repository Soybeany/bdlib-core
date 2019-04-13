package com.soybeany.bdlib.core.util.thread;

import com.soybeany.bdlib.core.java8.function.Consumer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Key-Value存储器
 * <br>Created by Soybeany on 2019/4/11.
 */
public class KeyValueStorage<Key, Value> {
    private final ConcurrentHashMap<Key, Value> mMap = new ConcurrentHashMap<>();

    public void put(Key key, Value value) {
        mMap.put(key, value);
    }

    public void putIfAbsent(Key key, Value value) {
        mMap.putIfAbsent(key, value);
    }

    public Value get(Key key) {
        return mMap.get(key);
    }

    /**
     * @return 是否执行了方法
     */
    public boolean invoke(Key key, Consumer<Value> consumer) {
        Value value = get(key);
        if (null != value) {
            consumer.accept(value);
            return true;
        }
        return false;
    }

    public Object remove(Key key) {
        return mMap.remove(key);
    }

    public void clear() {
        mMap.clear();
    }
}