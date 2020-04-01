package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络请求功能入口
 * <br>Created by Soybeany on 2019/4/11.
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public class OkHttpUtils {
    private static OkHttpClient PROTOTYPE = new OkHttpClient();

    /**
     * 设置原型客户端
     */
    public static void setupPrototypeClient(IClientSetter setter) {
        PROTOTYPE = getNewClient(setter);
    }

    /**
     * 获得新的自定义客户端
     */
    public static OkHttpClient getNewClient(IClientSetter setter) {
        OkHttpClient.Builder builder = PROTOTYPE.newBuilder();
        Optional.ofNullable(setter).ifPresent(s -> s.onSetup(builder));
        return builder.build();
    }

    /**
     * 取消指定标签的请求
     */
    public static void cancel(Object tag) {
        if (null == tag) {
            return;
        }
        Dispatcher dispatcher = PROTOTYPE.dispatcher();
        cancelCalls(dispatcher.queuedCalls(), tag);
        cancelCalls(dispatcher.runningCalls(), tag);
    }

    /**
     * 取消请求
     */
    private static void cancelCalls(List<Call> calls, Object tag) {
        for (Call call : calls) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public interface IClientSetter {
        static void setupTimeout(OkHttpClient.Builder builder, long sec) {
            builder.connectTimeout(sec, TimeUnit.SECONDS).readTimeout(sec, TimeUnit.SECONDS).writeTimeout(sec, TimeUnit.SECONDS);
        }

        void onSetup(OkHttpClient.Builder builder);
    }

    public interface IRequestSetter {
        Request onGetNewRequest(OkHttpRequestBuilder builder);
    }
}
