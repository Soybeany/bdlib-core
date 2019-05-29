package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.IExecutable;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;

import java.util.Set;

/**
 * 存储中心
 * <br>Created by Soybeany on 2019/5/29.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class StorageCenter<Key, Msg> {
    private KeySetStorage<Key, Info<Key, Msg>> CALLBACK_STORAGE = new KeySetStorage<>(); // 回调存储
    private KeySetStorage<ICallback<Msg>, Info<Key, Msg>> HANDLER_STORAGE = new KeySetStorage<>(); // 处理器存储
    private KeySetStorage<IExecutable, Info<Key, Msg>> HOLDER_STORAGE = new KeySetStorage<>(); // 持有器存储

    public void register(IExecutable holder, Key key, ICallback<Msg> callback) {
        // 创建信息
        Info<Key, Msg> info = new Info<>(key, holder, callback);
        // 回调映射中创建记录
        CALLBACK_STORAGE.putVal(key, info);
        // 处理器映射中创建记录
        HANDLER_STORAGE.putVal(callback, info);
        // 持有器中创建记录
        HOLDER_STORAGE.putVal(holder, info);
    }

    public void unregister(IExecutable holder) {
        Optional.ofNullable(HOLDER_STORAGE.get(holder)).ifPresent(this::removeRecords);
    }

    public void unregister(Key key) {
        Optional.ofNullable(CALLBACK_STORAGE.get(key)).ifPresent(this::removeRecords);
    }

    public void unregister(ICallback<Msg> callback) {
        Optional.ofNullable(HANDLER_STORAGE.get(callback)).ifPresent(this::removeRecords);
    }

    public void notifyNow(Key key, Msg data) {
        notifyDelay(key, data, 0);
    }

    public void notifyDelay(Key key, Msg data, long delayMills) {
        CALLBACK_STORAGE.invokeVal(key, info -> info.holder.post(() -> info.callback.onCall(data), delayMills));
    }

    /**
     * 停止指定名称的线程
     */
    public void stopThread(IExecutable holder) {
        holder.stop();
        // 移除相应的监听器
        unregister(holder);
    }

    private void removeRecords(Set<Info<Key, Msg>> infoSet) {
        for (Info<Key, Msg> info : infoSet) {
            CALLBACK_STORAGE.removeVal(info.key, info);
            HANDLER_STORAGE.removeVal(info.callback, info);
            HOLDER_STORAGE.removeVal(info.holder, info);
        }
    }

    /**
     * 强引用回调
     */
    public interface ICallback<Msg> {
        void onCall(Msg data);
    }

    private static class Info<Key, Msg> {
        Key key;
        IExecutable holder;
        ICallback<Msg> callback;

        Info(Key key, IExecutable holder, ICallback<Msg> callback) {
            this.key = key;
            this.holder = holder;
            this.callback = callback;
        }
    }
}
