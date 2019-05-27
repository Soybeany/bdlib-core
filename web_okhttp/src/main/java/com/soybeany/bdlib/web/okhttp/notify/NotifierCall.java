package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;
import com.soybeany.bdlib.web.okhttp.core.CallWrapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.CANCEL;
import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.ERROR;
import static com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason.NORM;

/**
 * 添加Notifier功能
 * <br>Created by Soybeany on 2019/5/7.
 */
@EverythingIsNonNull
public class NotifierCall extends CallWrapper {
    private final RequestNotifier mNotifier;

    public NotifierCall(Call target, RequestNotifier notifier) {
        super(target);
        mNotifier = notifier;
    }

    @Override
    public void enqueue(Callback callback) {
        if (callback instanceof NotifierCallback) {
            ((NotifierCallback) callback).setNotifier(mNotifier);
        }
        super.enqueue(new CallbackWrapper(callback));
    }

    @Override
    public Call clone() {
        return new NotifierCall(super.clone(), mNotifier);
    }

    public RequestNotifier getNotifier() {
        return mNotifier;
    }

    private class CallbackWrapper implements Callback {
        private Callback mTarget;
        private IOnCallListener mListener = msg -> RequestMsg.invokeWhenCancel(msg, NotifierCall.this::cancel);

        CallbackWrapper(Callback target) {
            mTarget = target;
            Optional.ofNullable(mNotifier).ifPresent(this::register);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            Optional.ofNullable(mNotifier).ifPresent(notifier -> unregister(notifier, call.isCanceled() ? CANCEL : ERROR));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            Optional.ofNullable(mNotifier).ifPresent(notifier -> unregister(notifier, NORM));
        }

        private void register(RequestNotifier notifier) {
            notifier.invoker().addListener(mListener);
            notifier.callback().notifyNow(new RequestMsg.OnStart());
        }

        private void unregister(RequestNotifier notifier, RequestFinishReason reason) {
            notifier.callback().notifyNow(new RequestMsg.OnFinish(reason));
            notifier.invoker().removeListener(mListener);
        }
    }
}