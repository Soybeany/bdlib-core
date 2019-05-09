package com.soybeany.bdlib.core.util.notify;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class Notifier implements MessageCenter.ICallback {
    private String mNotifyKey;

    public Notifier withNotifyKey(String notifyKey) {
        mNotifyKey = notifyKey;
        NotifyUtils.register(mNotifyKey, this);
        return this;
    }

    public void invoke(INotifyMsg.Invoker msg) {
        NotifyUtils.notifyNow(mNotifyKey, msg);
    }
}
