package com.soybeany.bdlib.core.util.file;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public class InvokeWrapper extends IProgressListener.IntervalWrapper {
    private long mLastInvoke;

    public InvokeWrapper(IProgressListener listener) {
        this(500, listener);
    }

    public InvokeWrapper(long minInterval, IProgressListener listener) {
        super(minInterval, listener);
    }

    @Override
    public void inProgress(float percent, long cur, long total) {
        long curTime = System.currentTimeMillis();
        if (curTime - mLastInvoke > getIntervalMills()) {
            super.inProgress(percent, cur, total);
            mLastInvoke = curTime;
        }
    }
}
