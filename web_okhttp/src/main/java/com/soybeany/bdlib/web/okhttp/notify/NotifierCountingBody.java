package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
public class NotifierCountingBody {

    public static class Request extends CountingRequestBody {
        public Request(RequestBody delegate, RequestNotifier notifier) {
            super(delegate);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RequestNotifierMsg.OnUpload())));
            }
        }
    }

    public static class Response extends CountingResponseBody {
        public Response(ResponseBody target, RequestNotifier notifier) {
            super(target);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RequestNotifierMsg.OnDownload())));
            }
        }
    }

    private static class NotifierListener implements IProgressListener {
        private final RequestNotifier mNotifier;
        private final RequestNotifierMsg.Callback<Float> mMsg;

        NotifierListener(RequestNotifier notifier, RequestNotifierMsg.Callback<Float> msg) {
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
