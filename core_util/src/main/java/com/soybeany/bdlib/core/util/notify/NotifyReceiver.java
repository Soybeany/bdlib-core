package com.soybeany.bdlib.core.util.notify;

/**
 * 通知接收者，在 通知转换器(Adapter) 内部中使用
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class NotifyReceiver implements MessageCenter.ICallback {
    private String mNotifyKey;

    public NotifyReceiver withNotifyKey(String notifyKey) {
        mNotifyKey = notifyKey;
        NotifyUtils.register(mNotifyKey, this);
        return this;
    }

    public void invoke(INotifyMsg.Invoker msg) {
        NotifyUtils.notifyNow(mNotifyKey, msg);
    }

    /**
     * 回调执行者，用于解析回调信息，并执行相应的回调
     */
    public interface ICallbackInvoker<Callback extends ICallback> {
        void signal(INotifyMsg msg, Callback callback);
    }

    /**
     * 相应的回调
     */
    public interface ICallback {
    }
}
