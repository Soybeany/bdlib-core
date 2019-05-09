package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.NotifyReceiver;

/**
 * 请求回调的调用者
 * <br>Created by Soybeany on 2019/5/9.
 */
public class RequestCallbackInvoker implements NotifyReceiver.ICallbackInvoker<RequestCallbackInvoker.ICallback> {
    @Override
    public void signal(INotifyMsg msg, ICallback callback) {
        switch (msg.getType()) {
            case RequestCallbackMsg.TYPE_ON_START:
                callback.onStart();
                break;
            case RequestCallbackMsg.TYPE_ON_UPLOAD:
                callback.onUpload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_DOWNLOAD:
                callback.onDownload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_FINISH:
                callback.onFinish((RequestFinishReason) msg.getData());
                break;
        }
    }

    public interface ICallback extends NotifyReceiver.ICallback {
        void onStart();

        void onUpload(float percent);

        void onDownload(float percent);

        void onFinish(RequestFinishReason reason);
    }
}
