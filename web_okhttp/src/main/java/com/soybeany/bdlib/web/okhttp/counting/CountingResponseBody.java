package com.soybeany.bdlib.web.okhttp.counting;


import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.file.ProgressRecorder;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public class CountingResponseBody extends ResponseBody {
    private final ResponseBody mDelegate;
    private final ProgressRecorder mRecorder;

    public CountingResponseBody(ResponseBody target, List<IProgressListener> listeners, RequestNotifier notifier) {
        mDelegate = target;
        mRecorder = new ProgressRecorder();
        for (IProgressListener listener : listeners) {
            mRecorder.add(listener);
        }
        Optional.ofNullable(notifier).ifPresent(n -> mRecorder.add(new NotifyListener(n, RequestCallbackMsg.TYPE_ON_DOWNLOAD)));
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        return mDelegate.contentLength();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public BufferedSource source() {
        return Okio.buffer(new CountingSource(mDelegate.source()));
    }

    private class CountingSource extends ForwardingSource {
        CountingSource(Source delegate) {
            super(delegate);
            mRecorder.start(contentLength());
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public long read(Buffer sink, long byteCount) throws IOException {
            long read = super.read(sink, byteCount);
            if (read > 0) {
                mRecorder.record(read);
            }
            return read;
        }

        @Override
        public void close() throws IOException {
            super.close();
            mRecorder.stop();
        }
    }
}
