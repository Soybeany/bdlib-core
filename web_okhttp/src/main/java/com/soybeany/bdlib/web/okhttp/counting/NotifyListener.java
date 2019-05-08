package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.notify.NotifyUtils;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
class NotifyListener implements IProgressListener {
    private final String mNotifyKey;
    private final RequestCallbackMsg mPercentMsg;

    NotifyListener(String notifyKey, String type) {
        mNotifyKey = notifyKey;
        mPercentMsg = new RequestCallbackMsg().type(type);
    }

    @Override
    public void inProgress(float percent, long cur, long total) {
        NotifyUtils.Dev.devNotifyNow(mNotifyKey, mPercentMsg.data(percent));
    }
}
