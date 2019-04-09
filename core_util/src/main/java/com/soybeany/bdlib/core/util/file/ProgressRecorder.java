package com.soybeany.bdlib.core.util.file;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.LinkedList;
import java.util.List;

import static com.soybeany.bdlib.core.util.file.IProgressListener.getPercent;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public class ProgressRecorder {
    private final List<IProgressListener> mListeners = new LinkedList<>();
    private final long UNSET = -1;

    private boolean mNeedFinalProgressCall = true;
    private long mTotal = UNSET;
    private long mCur;

    private Long mStartTimestamp;

    public ProgressRecorder add(IProgressListener listener) {
        Optional.ofNullable(listener).ifPresent(l -> mListeners.add(listener));
        return this;
    }

    public ProgressRecorder remove(IProgressListener listener) {
        Optional.ofNullable(listener).ifPresent(l -> mListeners.remove(listener));
        return this;
    }

    public ProgressRecorder finalProgressCall(boolean need) {
        mNeedFinalProgressCall = need;
        return this;
    }

    public void clear() {
        mListeners.clear();
    }

    public ProgressRecorder start(Long total) {
        mTotal = (null != total && total > 0) ? total : UNSET;
        mCur = 0;

        for (IProgressListener listener : mListeners) {
            listener.onStart();
        }
        mStartTimestamp = System.currentTimeMillis();
        return this;
    }

    public void stop() {
        if (null == mStartTimestamp) {
            throw new RuntimeException("记录器尚未开始");
        }
        // 按需调用最后一次进度
        if (mNeedFinalProgressCall) {
            record(mTotal - mCur);
        }
        // 调用结束回调
        long spend = System.currentTimeMillis() - mStartTimestamp;
        for (IProgressListener listener : mListeners) {
            listener.onFinish(spend);
        }
        mStartTimestamp = null;
    }

    public void record(long size) {
        mCur += size;
        for (IProgressListener listener : mListeners) {
            listener.inProgress(getPercent(mCur, mTotal), mCur, mTotal);
        }
    }
}
