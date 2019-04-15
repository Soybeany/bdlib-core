package com.soybeany.bdlib.core.util.storage;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.Set;

/**
 * 信息中心，适用于应用的数据通讯
 * 支持在任意线程中提交信息，并在任意线程中响应
 * <br>Created by Soybeany on 2018/2/22.
 */
// TODO: 2019/2/21 添加调用完成监听
public class MessageCenter {
    private static KeySetStorage<String, Info> CALLBACK_STORAGE = new KeySetStorage<>(); // 回调映射
    private static KeySetStorage<ICallback, Info> HANDLER_STORAGE = new KeySetStorage<>(); // 处理器映射
    private static KeySetStorage<IExecutable, Info> HOLDER_STORAGE = new KeySetStorage<>(); // 持有器映射

    public static void register(IExecutable holder, String key, ICallback callback) {
        // 创建信息
        Info info = new Info(key, holder, callback);
        // 回调映射中创建记录
        CALLBACK_STORAGE.putVal(key, info);
        // 处理器映射中创建记录
        HANDLER_STORAGE.putVal(callback, info);
        // 持有器中创建记录
        HOLDER_STORAGE.putVal(holder, info);
    }

    public static void unregister(IExecutable holder) {
        Optional.ofNullable(HOLDER_STORAGE.get(holder)).ifPresent(MessageCenter::removeRecords);
    }

    public static void unregister(String key) {
        Optional.ofNullable(CALLBACK_STORAGE.get(key)).ifPresent(MessageCenter::removeRecords);
    }

    public static void unregister(ICallback callback) {
        Optional.ofNullable(HANDLER_STORAGE.get(callback)).ifPresent(MessageCenter::removeRecords);
    }

    public static <Data> void notifyNow(String key, Data data) {
        notifyDelay(key, data, 0);
    }

    public static <Data> void notifyDelay(String key, Data data, long delayMills) {
        Optional.ofNullable(CALLBACK_STORAGE.get(key)).ifPresent(infoSet -> {
            for (Info info : infoSet) {
                info.holder.post(() -> info.callback.onCall(data), delayMills);
            }
        });
    }

    /**
     * 停止指定名称的线程
     */
    public static void stopThread(IExecutable holder) {
        holder.stop();
        // 移除相应的监听器
        unregister(holder);
    }

    private static void removeRecords(Set<Info> infoSet) {
        for (Info info : infoSet) {
            CALLBACK_STORAGE.removeVal(info.key, info);
            HANDLER_STORAGE.removeVal(info.callback, info);
            HOLDER_STORAGE.removeVal(info.holder, info);
        }
    }

    public interface ICallback {
        void onCall(Object data);
    }

    private static class Info {
        String key;
        IExecutable holder;
        ICallback callback;

        Info(String key, IExecutable holder, ICallback callback) {
            this.key = key;
            this.holder = holder;
            this.callback = callback;
        }
    }
}
