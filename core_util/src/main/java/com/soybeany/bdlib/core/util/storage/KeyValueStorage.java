package com.soybeany.bdlib.core.util.storage;

import com.soybeany.bdlib.core.java8.function.Consumer;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
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

    public Object remove(Key key) {
        return mMap.remove(key);
    }

    public Value get(Key key) {
        return mMap.get(key);
    }

    public boolean containKey(Key key) {
        return mMap.containsKey(key);
    }

    public boolean containValue(Value value) {
        return mMap.containsValue(value);
    }

    public void clear() {
        mMap.clear();
    }

    public Set<Key> keys() {
        HashSet<Key> set = new HashSet<>();
        Enumeration<Key> keys = mMap.keys();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;
    }

    public Collection<Value> values() {
        return mMap.values();
    }

    /**
     * 尝试取得Key对应的对象并执行其方法
     *
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

    /**
     * 执行全部存储对象的方法
     */
    public void invokeAll(Consumer<Value> consumer) {
        for (Value value : values()) {
            consumer.accept(value);
        }
    }
}