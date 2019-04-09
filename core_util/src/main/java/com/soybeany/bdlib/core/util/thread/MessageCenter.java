package com.soybeany.bdlib.core.util.thread;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 信息中心，适用于应用的数据通讯
 * 支持在任意线程中提交信息，并在任意线程中响应
 * <br>Created by Soybeany on 2018/2/22.
 */
// TODO: 2019/2/21 添加调用完成监听
public class MessageCenter {
    private static ConcurrentHashMap<String, Set<Info>> CALLBACK_MAP = new ConcurrentHashMap<>(); // 回调映射
    private static ConcurrentHashMap<ICallback, Set<Info>> HANDLER_MAP = new ConcurrentHashMap<>(); // 处理器映射
    private static ConcurrentHashMap<IExecutable, Set<Info>> HOLDER_MAP = new ConcurrentHashMap<>(); // 持有器映射

    public static void register(IExecutable holder, String key, ICallback callback) {
        // 创建信息
        Info info = new Info(key, holder, callback);
        // 回调映射中创建记录
        addRecord(CALLBACK_MAP, key, info);
        // 处理器映射中创建记录
        addRecord(HANDLER_MAP, callback, info);
        // 持有器中创建记录
        addRecord(HOLDER_MAP, holder, info);
    }

    public static void unregister(IExecutable holder) {
        Optional.ofNullable(HOLDER_MAP.get(holder)).ifPresent(MessageCenter::removeRecords);
    }

    public static void unregister(String key) {
        Optional.ofNullable(CALLBACK_MAP.get(key)).ifPresent(MessageCenter::removeRecords);
    }

    public static void unregister(ICallback callback) {
        Optional.ofNullable(HANDLER_MAP.get(callback)).ifPresent(MessageCenter::removeRecords);
    }

    public static <Data> void notify(String key, Data data, long delayMills) {
        Optional.ofNullable(CALLBACK_MAP.get(key)).ifPresent(infoSet -> {
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

    private static <Key> void addRecord(ConcurrentHashMap<Key, Set<Info>> map, Key key, Info info) {
        Set<Info> infoSet = map.get(key);
        if (null == infoSet) {
            map.putIfAbsent(key, new HashSet<>());
            infoSet = map.get(key);
        }
        infoSet.add(info);
    }

    private static void removeRecords(Set<Info> infoSet) {
        for (Info info : infoSet) {
            removeInfo(CALLBACK_MAP, info.key, info);
            removeInfo(HANDLER_MAP, info.callback, info);
            removeInfo(HOLDER_MAP, info.holder, info);
        }
    }

    private static <T> void removeInfo(ConcurrentHashMap<T, Set<Info>> map, T key, Info info) {
        Set<Info> infoSet = map.get(key);
        infoSet.remove(info);
        if (infoSet.isEmpty()) {
            map.remove(key);
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
