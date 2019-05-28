package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.file.ProgressRecorder;
import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
public class CountingRequestBody extends RequestBody implements IProgressListenerSetter.IApplier {
    private KeyValueStorage<BufferedSink, Sink> mSinkStorage = new KeyValueStorage<>();
    private final RequestBody mDelegate;
    private final Set<IProgressListener> mListeners = new HashSet<>();

    public CountingRequestBody(RequestBody delegate) {
        mDelegate = delegate;
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
        BufferedSink buffer = Okio.buffer(mSinkStorage.get(sink, () -> new CountingSink(sink, contentLength(), mListeners)));
        mDelegate.writeTo(buffer);
        buffer.flush();
    }

    @Override
    public CountingRequestBody listeners(IProgressListenerSetter setter) {
        setter.onSetup(mListeners);
        return this;
    }

    private static class CountingSink extends ForwardingSink {
        private final ProgressRecorder mRecorder = new ProgressRecorder();

        CountingSink(Sink delegate, long contentLength, Set<IProgressListener> listeners) {
            super(delegate);
            for (IProgressListener listener : listeners) {
                mRecorder.add(listener);
            }
            mRecorder.start(contentLength);
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