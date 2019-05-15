package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.IExecutable;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;

import java.util.Set;

/**
 * 信息中心，适用于应用的数据通讯
 * 支持在任意线程中提交信息，并在任意线程中响应
 * <br>Created by Soybeany on 2018/2/22.
 */
// TODO: 2019/2/21 添加调用完成监听
public class MessageCenter {
    private static KeySetStorage<String, Info> CALLBACK_STORAGE = new KeySetStorage<>(); // 强回调存储
    private static KeySetStorage<String, Info> WEEK_CALLBACK_STORAGE = new KeySetStorage<>(); // 弱回调存储
    private static KeySetStorage<ICallback, Info> HANDLER_STORAGE = new KeySetStorage<>(); // 处理器存储
    private static KeySetStorage<IExecutable, Info> HOLDER_STORAGE = new KeySetStorage<>(); // 持有器存储

    static {
        // 弱回调添加监听
        CALLBACK_STORAGE.addKeyRemoveListener(key -> {
            WEEK_CALLBACK_STORAGE.invokeVal(key, info -> ((IWeekCallback) info.callback).onKeyRemoved());
            WEEK_CALLBACK_STORAGE.remove(key);
        });
    }

    public static void register(IExecutable holder, String key, ICallback callback) {
        // 若没有强回调，不能注册弱回调
        if (callback instanceof IWeekCallback && !CALLBACK_STORAGE.containKey(key)) {
            return;
        }
        // 创建信息
        Info info = new Info(key, holder, callback);
        // 回调映射中创建记录
        if (callback instanceof IWeekCallback) {
            WEEK_CALLBACK_STORAGE.putVal(key, info);
        } else {
            CALLBACK_STORAGE.putVal(key, info);
        }
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
        Optional.ofNullable(WEEK_CALLBACK_STORAGE.get(key)).ifPresent(MessageCenter::removeRecords);
    }

    public static void unregister(ICallback callback) {
        Optional.ofNullable(HANDLER_STORAGE.get(callback)).ifPresent(MessageCenter::removeRecords);
    }

    public static void notifyNow(String key, Object data) {
        notifyDelay(key, data, 0);
    }

    public static void notifyDelay(String key, Object data, long delayMills) {
        innerNotify(CALLBACK_STORAGE, key, data, delayMills);
        innerNotify(WEEK_CALLBACK_STORAGE, key, data, delayMills);
    }

    /**
     * 停止指定名称的线程
     */
    public static void stopThread(IExecutable holder) {
        holder.stop();
        // 移除相应的监听器
        unregister(holder);
    }

    private static void innerNotify(KeySetStorage<String, Info> storage, String key, Object data, long delayMills) {
        Optional.ofNullable(storage.get(key)).ifPresent(infoSet -> {
            for (Info info : infoSet) {
                info.holder.post(() -> info.callback.onCall(data), delayMills);
            }
        });
    }

    private static void removeRecords(Set<Info> infoSet) {
        for (Info info : infoSet) {
            CALLBACK_STORAGE.removeVal(info.key, info);
            WEEK_CALLBACK_STORAGE.removeVal(info.key, info);
            HANDLER_STORAGE.removeVal(info.callback, info);
            HOLDER_STORAGE.removeVal(info.holder, info);
        }
    }

    /**
     * 强引用回调
     */
    public interface ICallback {
        void onCall(Object data);
    }

    /**
     * 弱引用回调，当没有强引用回调时，自动注销，不能注册
     */
    public interface IWeekCallback extends ICallback {
        default void onKeyRemoved() {
        }
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
