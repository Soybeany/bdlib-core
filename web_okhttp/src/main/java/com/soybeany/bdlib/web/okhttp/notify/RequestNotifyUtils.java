package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.MessageCenter;
import com.soybeany.bdlib.core.util.notify.NotifyUtils;

import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Callback.getRealKey;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class RequestNotifyUtils {

    public static void autoRegister(String notifyKey, Listener listener) {
        if (MessageCenter.containKey(getRealKey(notifyKey))) {
            NotifyUtils.register(notifyKey, listener.withNotifyKey(notifyKey));
        }
    }

    public static abstract class Listener implements MessageCenter.ICallback {
        private String mNotifyKey;

        @Override
        public void onCall(Object data) {
            INotifyMsg msg = (INotifyMsg) data;
            switch (msg.getType()) {
                case CallbackMsg.TYPE_ON_START:
                    onStart();
                    break;
                case CallbackMsg.TYPE_ON_UPLOAD:
                    onUpload((Float) msg.getData());
                    break;
                case CallbackMsg.TYPE_ON_DOWNLOAD:
                    onDownload((Float) msg.getData());
                    break;
                case CallbackMsg.TYPE_ON_FINISH:
                    onFinish();
                    MessageCenter.unregister(this);
                    break;
            }
        }

        Listener withNotifyKey(String notifyKey) {
            mNotifyKey = notifyKey;
            return this;
        }

        protected void invoke(InvokerMsg msg) {
            NotifyUtils.notifyNow(mNotifyKey, msg);
        }

        protected abstract void onStart();

        protected abstract void onUpload(float percent);

        protected abstract void onDownload(float percent);

        protected abstract void onFinish();
    }
}
