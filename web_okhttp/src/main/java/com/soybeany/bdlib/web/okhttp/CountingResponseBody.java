package com.soybeany.bdlib.web.okhttp;


import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.file.ProgressRecorder;

import java.io.IOException;

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
    private final IProgressListener mListener;

    public CountingResponseBody(ResponseBody target, IProgressListener listener) {
        mDelegate = target;
        mListener = listener;
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
        private final ProgressRecorder mRecorder = new ProgressRecorder().add(mListener);

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
