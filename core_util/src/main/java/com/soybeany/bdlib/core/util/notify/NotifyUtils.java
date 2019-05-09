package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.storage.IExecutable;

import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Callback;
import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Invoker;

/**
 * 用于解耦的通知模式
 * <br>Created by Soybeany on 2019/5/8.
 */
public class NotifyUtils {

    public static void autoRegister(String key, INotifier notifier) {
        register(key, notifier.withNotifyKey(key));
    }

    public static void register(String key, MessageCenter.ICallback callback) {
        MessageCenter.register(IExecutable.MULTI_WORK_THREAD, Callback.getRealKey(key), callback);
    }

    public static void unregister(MessageCenter.ICallback callback) {
        MessageCenter.unregister(callback);
    }

    public static void notifyNow(String key, Invoker msg) {
        MessageCenter.notifyNow(Invoker.getRealKey(key), msg);
    }

    public static class Dev {
        public static void devRegister(String key, MessageCenter.ICallback callback) {
            MessageCenter.register(IExecutable.MULTI_WORK_THREAD, Invoker.getRealKey(key), callback);
        }

        public static void devNotifyNow(String key, Callback msg) {
            MessageCenter.notifyNow(Callback.getRealKey(key), msg);
        }
    }

}
