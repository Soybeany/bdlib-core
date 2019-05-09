package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class RequestNotifyListener extends IRequestNotifier.Impl implements IRequestNotifier {

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
                MessageCenter.unregister(this);
                break;
        }
    }

    protected abstract void onStart();

    protected abstract void onUpload(float percent);

    protected abstract void onDownload(float percent);

    protected abstract void onFinish(RequestFinishReason reason);
}
