package com.soybeany.bdlib.core.util.file;

import com.soybeany.bdlib.core.java8.Optional;

/**
 * <br>Created by Soybeany on 2019/2/23.
 */
public interface IProgressListener {
    IProgressListener EMPTY = (percent, cur, total) -> {
    };

    static float getPercent(long cur, long total) {
        return total > 0 ? cur * 1.0f / total : -1;
    }

    default void onStart() {

    }

    void inProgress(float percent, long cur, long total);

    default void onFinish(long spend) {

    }

    class Wrapper implements IProgressListener {
        private final IProgressListener mListener;

        protected Wrapper(IProgressListener listener) {
            mListener = Optional.ofNullable(listener).orElse(EMPTY);
        }

        @Override
        public void onStart() {
            mListener.onStart();
        }

        @Override
        public void inProgress(float percent, long cur, long total) {
            mListener.inProgress(percent, cur, total);
        }

        @Override
        public void onFinish(long spend) {
            mListener.onFinish(spend);
        }
    }

    class IntervalWrapper extends Wrapper {
        private static final long MIN_INTERVAL_MILLS = 100;
        private final long mIntervalMills;

        IntervalWrapper(long intervalMills, IProgressListener listener) {
            super(listener);
            mIntervalMills = intervalMills > MIN_INTERVAL_MILLS ? intervalMills : MIN_INTERVAL_MILLS;
        }

        protected long getIntervalMills() {
            return mIntervalMills;
        }
    }
}
