package com.soybeany.bdlib.web.okhttp.counting;


import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.file.ProgressRecorder;
import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
public class CountingResponseBody extends ResponseBody implements IProgressListenerSetter.IApplier {
    private KeyValueStorage<BufferedSource, Source> mSourceStorage = new KeyValueStorage<>();
    private final ResponseBody mDelegate;
    private final Set<IProgressListener> mListeners = new HashSet<>();

    public CountingResponseBody(ResponseBody target) {
        mDelegate = target;
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
        return Okio.buffer(getSource());
    }

    @Override
    public CountingResponseBody listeners(IProgressListenerSetter setter) {
        setter.onSetup(mListeners);
        return this;
    }

    private Source getSource() {
        BufferedSource source = mDelegate.source();
        return mSourceStorage.get(source, () -> new CountingSource(source, contentLength(), mListeners));
    }

    private static class CountingSource extends ForwardingSource {
        private final ProgressRecorder mRecorder = new ProgressRecorder();

        CountingSource(Source delegate, long contentLength, Set<IProgressListener> listeners) {
            super(delegate);
            for (IProgressListener listener : listeners) {
                mRecorder.add(listener);
            }
            mRecorder.start(contentLength);
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
