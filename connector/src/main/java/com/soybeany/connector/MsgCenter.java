package com.soybeany.connector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class MsgCenter {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Map<Key, Set<IListener>> CALLBACKS = new ConcurrentHashMap<>();

    // //////////////////////////////////安全API//////////////////////////////////

    /**
     * 使用安全方式
     */
    public static void registerSafe(Key key, IListener listener) {
        EXECUTOR.execute(() -> register(key, listener));
    }

    /**
     *
     */
    public static void unregisterSafe(Key key, IListener listener) {
        EXECUTOR.execute(() -> unregister(key, listener));
    }

    // //////////////////////////////////普通API//////////////////////////////////

    /**
     * 注册监听器
     *
     * @return 是否注册成功
     */
    public static boolean register(Key key, IListener listener) {
        if (null == key || null == listener) {
            return false;
        }
        Lock lock = key.lock.writeLock();
        try {
            lock.lock();
            // 若Set未创建则创建
            Set<IListener> callbacks = CALLBACKS.get(key);
            if (null == callbacks) {
                CALLBACKS.put(key, callbacks = new HashSet<>());
            }
            // 添加回调
            callbacks.add(listener);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 注销监听器
     *
     * @return 是否注销成功
     */
    public static boolean unregister(Key key, IListener listener) {
        if (null == key || null == listener) {
            return false;
        }
        Lock lock = key.lock.writeLock();
        try {
            lock.lock();
            Set<IListener> callbacks = CALLBACKS.get(key);
            // 若没有匹配结果，直接返回
            if (null == callbacks) {
                return false;
            }
            // 移除回调
            callbacks.remove(listener);
            // 若Key已没有回调，则移除Set
            if (callbacks.isEmpty()) {
                CALLBACKS.remove(key);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送消息
     *
     * @return 是否发送成功
     */
    public static boolean sendMsg(Key key, Object msg) {
        Lock lock = key.lock.readLock();
        try {
            lock.lock();
            Set<IListener> callbacks = CALLBACKS.get(key);
            // 若没有匹配的回调则直接返回
            if (null == callbacks) {
                return false;
            }
            // 执行回调
            for (IListener callback : callbacks) {
                callback.onReceive(key, msg);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    // //////////////////////////////////内部类//////////////////////////////////

    public interface IListener {
        void onReceive(Key key, Object msg);
    }

    public static class Key {
        final ReadWriteLock lock = new ReentrantReadWriteLock();
    }

}
