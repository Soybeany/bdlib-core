package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.notify.CallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.NotifyUtils;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
class NotifyListener implements IProgressListener {
    private final String mNotifyKey;
    private final String mType;

    NotifyListener(String notifyKey, String type) {
        mNotifyKey = notifyKey;
        mType = type;
    }

    @Override
    public void inProgress(float percent, long cur, long total) {
        NotifyUtils.Dev.devNotifyNow(mNotifyKey, new CallbackMsg(mType, percent));
    }
}
