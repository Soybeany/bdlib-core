package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.MessageCenter;
import com.soybeany.bdlib.web.okhttp.notify.CallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.NotifyUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 * <br>Created by Soybeany on 2019/5/7.
 */
@EverythingIsNonNull
public class DefaultCall extends IRequestPart.CallWrapper {
    private final String mNotifyKey;

    public DefaultCall(Call target, String notifyKey) {
        super(target);
        mNotifyKey = notifyKey;
    }

    @Override
    public void enqueue(Callback responseCallback) {
        super.enqueue(new CallbackWrapper(responseCallback));
    }

    private class CallbackWrapper implements Callback {
        private Callback mTarget;
        private MessageCenter.ICallback mCallback = data -> cancel();

        CallbackWrapper(Callback target) {
            mTarget = target;
            Optional.ofNullable(mNotifyKey).ifPresent(key -> register());
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            Optional.ofNullable(mNotifyKey).ifPresent(key -> unregister());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            Optional.ofNullable(mNotifyKey).ifPresent(key -> unregister());
        }

        private void register() {
            NotifyUtils.Dev.devNotifyNow(mNotifyKey, new CallbackMsg(CallbackMsg.TYPE_ON_START, null));
            NotifyUtils.Dev.devRegister(mNotifyKey, mCallback);
        }

        private void unregister() {
            NotifyUtils.unregister(mCallback);
            NotifyUtils.Dev.devNotifyNow(mNotifyKey, new CallbackMsg(CallbackMsg.TYPE_ON_FINISH, null));
        }
    }
}
