package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.web.okhttp.core.NotifyRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;
import okio.Timeout;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IRequestPart {
    Call newCall(NotifyRequest request);

    @EverythingIsNonNull
    class CallWrapper implements Call {
        private Call mTarget;

        public CallWrapper(Call target) {
            mTarget = target;
        }

        @Override
        public Request request() {
            return mTarget.request();
        }

        @Override
        public Response execute() throws IOException {
            return mTarget.execute();
        }

        @Override
        public void enqueue(Callback responseCallback) {
            mTarget.enqueue(responseCallback);
        }

        @Override
        public void cancel() {
            mTarget.cancel();
        }

        @Override
        public boolean isExecuted() {
            return mTarget.isExecuted();
        }

        @Override
        public boolean isCanceled() {
            return mTarget.isCanceled();
        }

        @Override
        public Timeout timeout() {
            return mTarget.timeout();
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Call clone() {
            return new CallWrapper(mTarget.clone());
        }
    }

}
