package com.soybeany.bdlib.core.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 遍历器工具类
 * <br>Created by Soybeany on 2019/3/7.
 */
public class IterableUtils {

    /**
     * 进行遍历(不带返回值)
     */
    public static <T> void forEach(Iterable<T> iterable, IVoidCallback<T> callback) {
        forEachInner(iterable, callback, (list, obj, flag) -> {
            try {
                callback.onConsume(obj, flag);
                list.add(obj);
            } catch (Exception e) {
                callback.OnException(e, flag);
            }
        });
    }

    /**
     * 进行遍历(具有返回值)
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R forEach(Iterable<T> iterable, R firstReturn, IReturnCallback<R, T> callback) {
        ReturnConsumer<R, T> consumer = new ReturnConsumer<>(firstReturn, callback);
        forEachInner(iterable, callback, consumer);
        return consumer.getLastReturn();
    }

    private static <T> void forEachInner(Iterable<T> iterable, ICallback<T> callback, IConsumer<T> consumer) {
        if (null == iterable || null == callback) {
            return;
        }
        List<T> handledList = new LinkedList<>();
        try {
            Flag flag = new Flag();
            for (T t : callback.onInit(iterable)) {
                consumer.onConsume(handledList, t, flag);
                if (!flag.needGoOn) {
                    break;
                }
            }
        } finally {
            callback.onFinish(handledList);
        }
    }

    private static class ReturnConsumer<R, T> implements IConsumer<T> {
        private R mLastReturn;
        private IReturnCallback<R, T> mCallback;

        ReturnConsumer(R firstReturn, IReturnCallback<R, T> callback) {
            mLastReturn = firstReturn;
            mCallback = callback;
        }

        @Override
        public void onConsume(List<T> handledList, T obj, Flag flag) {
            try {
                mLastReturn = mCallback.onConsume(obj, mLastReturn, flag);
            } catch (Exception e) {
                throw (e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e.getMessage()));
            }
        }

        R getLastReturn() {
            return mLastReturn;
        }
    }

    public interface IVoidCallback<T> extends ICallback<T> {
        void onConsume(T obj, Flag flag) throws Exception;

        default void OnException(Exception e, Flag flag) {
        }
    }

    public interface IReturnCallback<R, T> extends ICallback<T> {
        R onConsume(T obj, R lastReturn, Flag flag) throws Exception;
    }

    public static class Flag {
        public boolean needGoOn = true;
    }

    private interface ICallback<T> {
        default Iterable<T> onInit(Iterable<T> iterable) {
            return iterable;
        }

        default void onFinish(List<T> handledList) {
        }
    }

    private interface IConsumer<T> {
        void onConsume(List<T> handledList, T obj, Flag flag);
    }
}
