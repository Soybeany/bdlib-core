package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.MessageCenter;
import com.soybeany.bdlib.core.util.storage.IExecutable;

import static com.soybeany.bdlib.core.util.notify.INotifyMsg.Callback.getRealKey;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class RequestNotifyUtils {

    public static void autoRegister(IExecutable executable, String notifyKey, Listener listener) {
        if (!MessageCenter.containKey(getRealKey(notifyKey))) {
            return;
        }
        MessageCenter.register(executable, notifyKey, listener);
    }

    public class Listener implements MessageCenter.ICallback {
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

        protected void onStart() {
        }

        protected void onUpload(float percent) {
        }

        protected void onDownload(float percent) {
        }

        protected void onFinish() {
        }
    }
}
