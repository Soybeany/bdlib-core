package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.file.ProgressRecorder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public class CountingRequestBody extends RequestBody {
    private final RequestBody mDelegate;
    private final IProgressListener mListener;

    public CountingRequestBody(RequestBody delegate, IProgressListener listener) {
        mDelegate = delegate;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink buffer = Okio.buffer(new CountingSink(sink));
        mDelegate.writeTo(buffer);
        buffer.flush();
    }

    private class CountingSink extends ForwardingSink {
        private final ProgressRecorder mRecorder = new ProgressRecorder().add(mListener);

        CountingSink(Sink delegate) {
            super(delegate);
            mRecorder.start(contentLength());
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            mRecorder.record(byteCount);
        }

        @Override
        public void close() throws IOException {
            super.close();
            mRecorder.stop();
        }
    }
}