package com.soybeany.bdlib.core.util.center;

import com.soybeany.bdlib.core.util.storage.IExecutable;

/**
 * 信息中心，适用于应用的数据通讯
 * 支持在任意线程中提交信息，并在任意线程中响应
 * <br>Created by Soybeany on 2018/2/22.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MessageCenter {
    private static StorageCenter<String, Object> INSTANCE = new StorageCenter<>();

    public static void register(IExecutable holder, String key, ICallback callback) {
        INSTANCE.register(holder, key, callback);
    }

    public static void unregister(IExecutable holder) {
        INSTANCE.unregister(holder);
    }

    public static void unregister(String key) {
        INSTANCE.unregister(key);
    }

    public static void unregister(ICallback callback) {
        INSTANCE.unregister(callback);
    }

    public static void notifyNow(String key, Object data) {
        INSTANCE.notifyNow(key, data);
    }

    public static void notifyDelay(String key, Object data, long delayMills) {
        INSTANCE.notifyDelay(key, data, delayMills);
    }

    /**
     * 停止指定名称的线程
     */
    public static void stopThread(IExecutable holder) {
        INSTANCE.stopThread(holder);
    }

    /**
     * 强引用回调
     */
    public interface ICallback extends StorageCenter.ICallback<String, Object> {
    }
}
