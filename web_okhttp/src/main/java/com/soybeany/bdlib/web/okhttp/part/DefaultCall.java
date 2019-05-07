package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.storage.IExecutable;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 * <br>Created by Soybeany on 2019/5/7.
 */
@EverythingIsNonNull
public class DefaultCall extends IRequestPart.CallWrapper {
    private final List<IStateListener> mListeners;

    public DefaultCall(Call target, List<IStateListener> listeners) {
        super(target);
        mListeners = listeners;
    }

    @Override
    public void enqueue(Callback responseCallback) {
        super.enqueue(new CallbackWrapper(responseCallback));
    }

    private class CallbackWrapper implements Callback {
        private final String mCancelUid = "CANCEL-UID:" + FileUtils.getUUID();
        private Callback mTarget;

        CallbackWrapper(Callback target) {
            mTarget = target;
            register();
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            unregister();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            unregister();
        }

        private void register() {
            IterableUtils.forEach(mListeners, (listener, flag) -> listener.onStart(mCancelUid));
            MessageCenter.register(IExecutable.MULTI_WORK_THREAD, mCancelUid, data -> cancel());
        }

        private void unregister() {
            MessageCenter.unregister(mCancelUid);
            IterableUtils.forEach(mListeners, (listener, flag) -> listener.onFinish());
        }
    }
}
