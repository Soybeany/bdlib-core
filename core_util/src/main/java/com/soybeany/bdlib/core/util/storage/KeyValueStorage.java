package com.soybeany.bdlib.core.util.storage;

import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.java8.function.Supplier;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Key-Value存储器
 * <br>Created by Soybeany on 2019/4/11.
 */
public class KeyValueStorage<Key, Value> {
    private final ConcurrentMap<Key, Value> mMap = new ConcurrentHashMap<>();

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

    public Value get(Key key, Supplier<? extends Value> other) {
        Value value = get(key);
        if (null != value) {
            return value;
        }
        putIfAbsent(key, other.get());
        return get(key);
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
        return mMap.keySet();
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