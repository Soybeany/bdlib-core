package com.soybeany.bdlib.core.util.thread;

import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.file.FileUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象存储器
 * <br>Created by Soybeany on 2019/4/11.
 */
public class ObjectStorage {
    public static final ObjectStorage SINGLETON = new ObjectStorage();

    private final Map<String, Object> mMap = new ConcurrentHashMap<>();

    public String put(Object obj) {
        String uuid = FileUtils.getUUID();
        put(uuid, obj);
        return uuid;
    }

    public void put(String key, Object obj) {
        mMap.put(key, obj);
    }

    public Object get(String key) {
        return mMap.get(key);
    }

    /**
     * @return 是否执行了方法
     */
    @SuppressWarnings("unchecked")
    public <T> boolean invoke(String key, Consumer<T> consumer, Class<T> clazz) {
        T t = (T) get(key);
        if (null != t) {
            consumer.accept(t);
            return true;
        }
        return false;
    }

    public Object remove(String key) {
        return mMap.remove(key);
    }

    public void clear() {
        mMap.clear();
    }
}
