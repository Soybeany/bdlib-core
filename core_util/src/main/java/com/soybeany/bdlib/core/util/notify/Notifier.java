package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 通知者，需在合适的地方手动调用{@link #register()}与{@link #unregister()}
 * <br>Created by Soybeany on 2019/5/11.
 */
public class Notifier implements MessageCenter.ICallback {
    private Set<IOnCallDealer> mDealers = new HashSet<>();
    private String mNotifyKey = FileUtils.getUUID();

    @Override
    public void onCall(Object data) {
        INotifyMsg.invokeIfIsNotifyMsg(data, msg -> IterableUtils.forEach(mDealers, (dealer, flag) -> dealer.onCall(msg)));
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

    public void register() {
        NotifyUtils.Dev.devRegister(mNotifyKey, this);
    }

    public void unregister() {
        NotifyUtils.unregister(this);
    }

    public void invoke(INotifyMsg.Invoker msg) {
        NotifyUtils.notifyNow(mNotifyKey, msg);
    }
}
