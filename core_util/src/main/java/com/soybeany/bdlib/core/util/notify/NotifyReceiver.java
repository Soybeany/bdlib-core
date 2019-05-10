package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 通知接收者，在 通知连接桥(Bridge) 内部中使用
 * <br>Created by Soybeany on 2019/5/9.
 */
public class NotifyReceiver implements MessageCenter.ICallback {
    private List<ICallDealer> mDealers = new LinkedList<>();
    private String mNotifyKey;

    public NotifyReceiver withNotifyKey(String notifyKey) {
        mNotifyKey = notifyKey;
        NotifyUtils.register(mNotifyKey, this);
        return this;
    }

    @Override
    public void onCall(Object data) {
        IterableUtils.forEach(mDealers, (dealer, flag) -> dealer.onCall((INotifyMsg) data));
    }

    public void invoke(INotifyMsg.Invoker msg) {
        NotifyUtils.notifyNow(mNotifyKey, msg);
    }

    public void addDealer(ICallDealer dealer) {
        mDealers.add(dealer);
    }

    public void removeDealer(ICallDealer dealer) {
        mDealers.remove(dealer);
    }

    /**
     * 回调执行者，用于解析回调信息，并执行相应的回调
     */
    public interface ICallDealer {
        void onCall(INotifyMsg msg);
    }
}
