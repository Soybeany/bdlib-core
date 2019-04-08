package com.soybeany.bdlib.core.util.file;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public class TimerWrapper extends IProgressListener.IntervalWrapper {
    private ScheduledExecutorService mService = Executors.newSingleThreadScheduledExecutor();

    private float mPercent;
    private long mCur;
    private long mTotal;

    TimerWrapper(long intervalMills, IProgressListener listener) {
        super(intervalMills, listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mService.scheduleWithFixedDelay(this::invokeSuperInProgress, 0, getIntervalMills(), TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void inProgress(float percent, long cur, long total) {
        mPercent = percent;
        mCur = cur;
        mTotal = total;
    }

    @Override
    public void onFinish(long spend) {
        super.onFinish(spend);
        mService.shutdown();
    }

    private synchronized void invokeSuperInProgress() {
        super.inProgress(mPercent, mCur, mTotal);
    }
}
