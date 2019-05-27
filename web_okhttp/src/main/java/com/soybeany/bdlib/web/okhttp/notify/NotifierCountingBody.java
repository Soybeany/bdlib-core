package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
public class NotifierCountingBody {

    public static class Request extends CountingRequestBody {
        public Request(RequestBody delegate, List<IProgressListener> listeners, RequestNotifier notifier) {
            super(delegate, listeners);
            Optional.ofNullable(notifier).ifPresent(n -> mRecorder.add(new NotifierListener(n, new RequestMsg.OnUpload())));
        }
    }

    public static class Response extends CountingResponseBody {
        public Response(ResponseBody target, List<IProgressListener> listeners, RequestNotifier notifier) {
            super(target, listeners);
            Optional.ofNullable(notifier).ifPresent(n -> mRecorder.add(new NotifierListener(n, new RequestMsg.OnDownload())));
        }
    }

    private static class NotifierListener implements IProgressListener {
        private final RequestNotifier mNotifier;
        private final RequestMsg.Callback<Float> mMsg;

        NotifierListener(RequestNotifier notifier, RequestMsg.Callback<Float> msg) {
            mNotifier = notifier;
            mMsg = msg;
        }

        @Override
        public void inProgress(float percent, long cur, long total) {
            mMsg.setData(percent);
            mNotifier.callback().notifyNow(mMsg);
        }
    }
}
