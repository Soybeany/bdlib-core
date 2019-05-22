package com.soybeany.bdlib.web.okhttp.core;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 * 添加快速失败功能，避免请求取消时，偶尔回调过慢的问题
 * <br>Created by Soybeany on 2019/5/22.
 */
@EverythingIsNonNull
public class FastFailCall extends CallWrapper {
    private static final Callback DO_NOTHING_CALLBACK = new CallbackImpl();

    private Callback mCallback = DO_NOTHING_CALLBACK;

    public FastFailCall(Call target) {
        super(target);
    }

    @Override
    public void enqueue(Callback responseCallback) {
        super.enqueue(mCallback = new CallbackWrapper(responseCallback));
    }

    @Override
    public void cancel() {
        super.cancel();
        mCallback.onFailure(this, new IOException("取消"));
    }

    @Override
    public Call clone() {
        return new FastFailCall(super.clone());
    }

    private static class CallbackWrapper implements Callback {
        private Callback mTarget;
        private boolean mIsSignaled;

        CallbackWrapper(Callback target) {
            mTarget = target;
        }

        @Override
        public synchronized void onFailure(Call call, IOException e) {
            if (mIsSignaled) {
                return;
            }
            mIsSignaled = true;
            mTarget.onFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
        }
    }

    private static class CallbackImpl implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            // 留空
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            // 留空
        }
    }
}
