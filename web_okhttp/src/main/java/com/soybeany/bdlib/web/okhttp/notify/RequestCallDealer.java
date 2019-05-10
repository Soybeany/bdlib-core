package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.NotifyReceiver;

/**
 * 请求回调的处理者
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class RequestCallDealer implements NotifyReceiver.ICallDealer {
    @Override
    public void onCall(INotifyMsg msg) {
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

    public static class Empty extends RequestCallDealer {
        @Override
        protected void onStart() {
        }

        @Override
        protected void onUpload(float percent) {
        }

        @Override
        protected void onDownload(float percent) {
        }

        @Override
        protected void onFinish(RequestFinishReason reason) {
        }
    }
}
