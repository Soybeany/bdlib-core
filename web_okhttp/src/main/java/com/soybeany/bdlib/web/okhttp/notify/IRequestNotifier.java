package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.MessageCenter;
import com.soybeany.bdlib.core.util.notify.NotifyUtils;

import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Callback.getRealKey;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public interface IRequestNotifier extends MessageCenter.ICallback {

    static void autoRegister(String notifyKey, IRequestNotifier listener) {
        if (MessageCenter.containKey(getRealKey(notifyKey))) {
            NotifyUtils.register(notifyKey, listener.withNotifyKey(notifyKey));
        }
    }

    IRequestNotifier withNotifyKey(String notifyKey);

    void invoke(RequestInvokerMsg msg);

    /**
     * 默认实现
     */
    abstract class Impl implements IRequestNotifier {
        private String mNotifyKey;

        @Override
        public IRequestNotifier withNotifyKey(String notifyKey) {
            mNotifyKey = notifyKey;
            return this;
        }

        @Override
        public void invoke(RequestInvokerMsg msg) {
            NotifyUtils.notifyNow(mNotifyKey, msg);
        }
    }
}
