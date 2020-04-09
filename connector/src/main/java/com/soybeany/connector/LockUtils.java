package com.soybeany.connector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * 用于有添加、删除监听器以及发送消息功能的场景
 * <br>Created by Soybeany on 2020/4/9.
 */
public class LockUtils {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * 执行任务
     *
     * @param lock       使用的锁
     * @param task       待执行的任务
     * @param inSafeMode 若启用，则使用一个额外的线程来执行此任务（不能是耗时任务，因为是全局单线程），避免产生死锁
     */
    public static void execute(Lock lock, Runnable task, boolean inSafeMode) {
        if (inSafeMode) {
            EXECUTOR.execute(() -> innerExecute(lock, task));
        } else {
            innerExecute(lock, task);
        }
    }

    private static void innerExecute(Lock lock, Runnable task) {
        try {
            lock.lock();
            task.run();
        } finally {
            lock.unlock();
        }
    }
}
