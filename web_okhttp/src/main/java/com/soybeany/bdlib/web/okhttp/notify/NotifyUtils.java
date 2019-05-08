package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.storage.IExecutable;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class NotifyUtils {

    public static void register(String key, MessageCenter.ICallback callback) {
        MessageCenter.register(IExecutable.MULTI_WORK_THREAD, CallbackMsg.getRealKey(key), callback);
    }

    public static void unregister(MessageCenter.ICallback callback) {
        MessageCenter.unregister(callback);
    }

    public static void notifyNow(String key, InvokerMsg msg) {
        MessageCenter.notifyNow(InvokerMsg.getRealKey(key), msg);
    }

    public static class Dev {
        public static void devRegister(String key, MessageCenter.ICallback callback) {
            MessageCenter.register(IExecutable.MULTI_WORK_THREAD, InvokerMsg.getRealKey(key), callback);
        }

        public static void devNotifyNow(String key, CallbackMsg msg) {
            MessageCenter.notifyNow(CallbackMsg.getRealKey(key), msg);
        }
    }
}
