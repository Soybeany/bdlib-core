package com.soybeany.connector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings({"WeakerAccess"})
public class MsgCenter {

    private static final Map<Key, Set<IListener>> CALLBACKS = new ConcurrentHashMap<>();

    // //////////////////////////////////API//////////////////////////////////

    /**
     * 注册监听器，若要在sendMsg过程中调用此方法，需使用安全模式(使用子线程)，可避免死锁
     */
    public static void register(Key key, IListener listener, boolean inSafeMode) {
        if (null == key || null == listener) {
            return;
        }
        LockUtils.execute(key.lock.writeLock(), () -> {
            // 若Set未创建则创建
            Set<IListener> callbacks = CALLBACKS.get(key);
            if (null == callbacks) {
                CALLBACKS.put(key, callbacks = new HashSet<>());
            }
            // 添加回调
            callbacks.add(listener);
        }, inSafeMode);
    }

    /**
     * 注销监听器，若要在sendMsg过程中调用此方法，需使用安全模式(使用子线程)，可避免死锁
     */
    public static void unregister(Key key, IListener listener, boolean inSafeMode) {
        if (null == key || null == listener) {
            return;
        }
        LockUtils.execute(key.lock.writeLock(), () -> {
            Set<IListener> callbacks = CALLBACKS.get(key);
            // 若没有匹配结果，直接返回
            if (null == callbacks) {
                return;
            }
            // 移除回调
            callbacks.remove(listener);
            // 若Key已没有回调，则移除Set
            if (callbacks.isEmpty()) {
                CALLBACKS.remove(key);
            }
        }, inSafeMode);
    }

    /**
     * 发送消息
     */
    public static void sendMsg(Key key, Object msg) {
        LockUtils.execute(key.lock.readLock(), () -> {
            Set<IListener> callbacks = CALLBACKS.get(key);
            // 若没有匹配的回调则直接返回
            if (null == callbacks) {
                return;
            }
            // 执行回调
            for (IListener callback : callbacks) {
                callback.onReceive(key, msg);
            }
        }, false);
    }

    // //////////////////////////////////内部类//////////////////////////////////

    public interface IListener {
        void onReceive(Key key, Object msg);
    }

    public static class Key {
        final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    }

}
