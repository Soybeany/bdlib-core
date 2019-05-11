package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.notify.INotifyMsg.Callback;
import com.soybeany.bdlib.core.util.storage.IExecutable;

import java.util.HashSet;
import java.util.Set;

import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Invoker;
import static com.soybeany.bdlib.core.util.notify.INotifyMsg.invokeIfIsNotifyMsg;

/**
 * 通知者，需在合适的地方手动调用{@link #registerDealers}与{@link #unregisterDealers}
 * <br>Created by Soybeany on 2019/5/11.
 */
public class Notifier implements MessageCenter.ICallback {
    private final Set<IOnCallDealer> mDealers = new HashSet<>();
    private final String mNotifyKey = FileUtils.getUUID();

    @Override
    public void onCall(Object data) {
        invokeIfIsNotifyMsg(data, msg -> IterableUtils.forEach(mDealers, (dealer, flag) -> dealer.onCall(msg)));
    }

    public String getNotifyKey() {
        return mNotifyKey;
    }

    public void addDealer(IOnCallDealer dealer) {
        mDealers.add(dealer);
    }

    public void removeDealer(IOnCallDealer dealer) {
        mDealers.remove(dealer);
    }

    public void register(MessageCenter.ICallback callback) {
        innerRegister(Callback.getRealKey(mNotifyKey), callback);
    }

    public void devRegister(MessageCenter.ICallback callback) {
        innerRegister(Invoker.getRealKey(mNotifyKey), callback);
    }

    public void registerDealers() {
        devRegister(this);
    }

    public void unregister(MessageCenter.ICallback callback) {
        MessageCenter.unregister(callback);
    }

    public void unregisterDealers() {
        unregister(this);
    }

    public void notifyNow(Invoker msg) {
        MessageCenter.notifyNow(Invoker.getRealKey(mNotifyKey), msg);
    }

    public void devNotifyNow(Callback msg) {
        MessageCenter.notifyNow(Callback.getRealKey(mNotifyKey), msg);
    }

    private void innerRegister(String key, MessageCenter.ICallback callback) {
        MessageCenter.register(IExecutable.MULTI_WORK_THREAD, key, callback);
    }
}
