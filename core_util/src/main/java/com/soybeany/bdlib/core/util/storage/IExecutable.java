package com.soybeany.bdlib.core.util.storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface IExecutable {

    IExecutable SINGLE_WORK_THREAD = ThreadPoolImpl.getSingleNew();
    IExecutable MULTI_WORK_THREAD = ThreadPoolImpl.getMultiNew();

    void post(Runnable runnable, long delayMills);

    void stop();

    class ThreadPoolImpl implements IExecutable {

        private static final ScheduledExecutorService TIMER = Executors.newSingleThreadScheduledExecutor();

        private ExecutorService mService;

        public static IExecutable getSingleNew() {
            return getNew(Executors.newSingleThreadExecutor());
        }

        public static IExecutable getMultiNew() {
            return getNew(Executors.newCachedThreadPool());
        }

        public static IExecutable getNew(ExecutorService service) {
            return new ThreadPoolImpl(service);
        }

        private ThreadPoolImpl(ExecutorService service) {
            mService = service;
        }

        @Override
        public void post(Runnable runnable, long delayMills) {
            TIMER.schedule(() -> mService.submit(runnable), delayMills, TimeUnit.SECONDS);
        }

        @Override
        public void stop() {
            mService.shutdown();
        }
    }
}