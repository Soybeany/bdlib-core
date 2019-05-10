package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.MessageCenter;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.core.util.notify.NotifyUtils;
import com.soybeany.bdlib.web.okhttp.core.CallWrapper;
import com.soybeany.bdlib.web.okhttp.core.INotifyKeyReceiver;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

import static com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg.TYPE_ON_FINISH;
import static com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg.TYPE_ON_START;
import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.CANCEL;
import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.ERROR;
import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.NORM;

/**
 * <br>Created by Soybeany on 2019/5/7.
 */
@EverythingIsNonNull
public class NotifyCall extends CallWrapper {
    private final Notifier mNotifier;
    private final String mNotifyKey;

    public NotifyCall(Call target, Notifier notifier) {
        super(target);
        mNotifier = notifier;
        mNotifyKey = notifier.getNotifyKey();
    }

    @Override
    public void enqueue(Callback callback) {
        if (callback instanceof INotifyKeyReceiver) {
            ((INotifyKeyReceiver) callback).onReceive(mNotifyKey);
        }
        super.enqueue(new CallbackWrapper(callback));
    }

    @Override
    public Call clone() {
        return new NotifyCall(cloneTarget(), mNotifier);
    }

    private class CallbackWrapper implements Callback {
        private Callback mTarget;
        private MessageCenter.ICallback mCallback = data -> RequestInvokerMsg.invokeOnCancel(data, NotifyCall.this::cancel);
        private RequestCallbackMsg mMsg = new RequestCallbackMsg();

        CallbackWrapper(Callback target) {
            mTarget = target;
            Optional.ofNullable(mNotifyKey).ifPresent(key -> register());
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            Optional.ofNullable(mNotifyKey).ifPresent(key -> unregister(call.isCanceled() ? CANCEL : ERROR));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            Optional.ofNullable(mNotifyKey).ifPresent(key -> unregister(NORM));
        }

        private void register() {
            NotifyUtils.Dev.devRegister(mNotifyKey, mCallback);
            mNotifier.register();
            NotifyUtils.Dev.devNotifyNow(mNotifyKey, mMsg.type(TYPE_ON_START));
        }

        private void unregister(RequestFinishReason reason) {
            NotifyUtils.Dev.devNotifyNow(mNotifyKey, mMsg.type(TYPE_ON_FINISH).data(reason));
            mNotifier.unregister();
            NotifyUtils.unregister(mCallback);
        }
    }
}
