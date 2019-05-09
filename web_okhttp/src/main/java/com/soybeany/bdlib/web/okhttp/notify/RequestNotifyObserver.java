package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifier;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class RequestNotifyObserver extends INotifier.Impl {
    @Override
    public void onCall(Object data) {
        INotifyMsg msg = (INotifyMsg) data;
        switch (msg.getType()) {
            case RequestCallbackMsg.TYPE_ON_START:
                onStart();
                break;
            case RequestCallbackMsg.TYPE_ON_UPLOAD:
                onUpload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_DOWNLOAD:
                onDownload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_FINISH:
                onFinish((RequestFinishReason) msg.getData());
                break;
        }
    }

    protected abstract void onStart();

    protected abstract void onUpload(float percent);

    protected abstract void onDownload(float percent);

    protected abstract void onFinish(RequestFinishReason reason);
}
