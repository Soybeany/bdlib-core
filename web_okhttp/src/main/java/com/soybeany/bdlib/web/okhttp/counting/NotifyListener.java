package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
class NotifyListener implements IProgressListener {
    private final Notifier mNotifier;
    private final RequestCallbackMsg mPercentMsg = new RequestCallbackMsg();

    NotifyListener(Notifier notifier, String type) {
        mNotifier = notifier;
        mPercentMsg.type(type);
    }

    @Override
    public void inProgress(float percent, long cur, long total) {
        mNotifier.devNotifyNow(mPercentMsg.data(percent));
    }
}
