package com.soybeany.bdlib.core.util.notify;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public interface INotifier extends MessageCenter.IWeekCallback {
    INotifier withNotifyKey(String notifyKey);

    void invoke(INotifyMsg.Invoker msg);

    abstract class Impl implements INotifier {
        private String mNotifyKey;

        @Override
        public INotifier withNotifyKey(String notifyKey) {
            mNotifyKey = notifyKey;
            NotifyUtils.register(mNotifyKey, this);
            return this;
        }

        @Override
        public void invoke(INotifyMsg.Invoker msg) {
            NotifyUtils.notifyNow(mNotifyKey, msg);
        }
    }
}
