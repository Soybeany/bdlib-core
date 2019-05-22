package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;
import com.soybeany.bdlib.web.okhttp.core.CallWrapper;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;

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
    private final RequestNotifier mNotifier;

    public NotifyCall(Call target, RequestNotifier notifier) {
        super(target);
        mNotifier = notifier;
    }

    @Override
    public void enqueue(Callback callback) {
        if (callback instanceof OkHttpCallback) {
            ((OkHttpCallback) callback).setNotifier(mNotifier);
        }
        super.enqueue(new CallbackWrapper(callback));
    }

    @Override
    public Call clone() {
        return new NotifyCall(cloneTarget(), mNotifier);
    }

    public RequestNotifier getNotifier() {
        return mNotifier;
    }

    private class CallbackWrapper implements Callback {
        private Callback mTarget;
        private IOnCallListener mListener = msg -> RequestInvokerMsg.invokeOnCancel(msg, NotifyCall.this::cancel);
        private RequestCallbackMsg mMsg = new RequestCallbackMsg();

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
            notifier.callback().notifyNow(mMsg.type(TYPE_ON_START));
        }

        private void unregister(RequestNotifier notifier, RequestFinishReason reason) {
            notifier.callback().notifyNow(mMsg.type(TYPE_ON_FINISH).data(reason));
            notifier.invoker().removeListener(mListener);
        }
    }
}
