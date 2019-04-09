package com.soybeany.bdlib.web.okhttp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient工厂
 * <br>Created by Soybeany on 2019/4/9.
 */
public class OkHttpClientFactory {

    private static OkHttpClient DEFAULT_CLIENT = new OkHttpClient();

    /**
     * 获得默认客户端
     */
    public static OkHttpClient getDefaultClient() {
        return DEFAULT_CLIENT;
    }

    /**
     * 设置默认的客户端
     */
    public static void setupDefaultClient(IBuilderSetter setter) {
        DEFAULT_CLIENT = getNewClient(setter);
    }

    /**
     * 设置超时
     */
    public static void setupTimeout(OkHttpClient.Builder builder, long sec) {
        builder.connectTimeout(sec, TimeUnit.SECONDS).readTimeout(sec, TimeUnit.SECONDS).writeTimeout(sec, TimeUnit.SECONDS);
    }

    /**
     * 取消指定标签的请求
     */
    public static void cancel(Object tag) {
        if (null == tag) {
            return;
        }
        Dispatcher dispatcher = DEFAULT_CLIENT.dispatcher();
        cancelCalls(dispatcher.queuedCalls(), tag);
        cancelCalls(dispatcher.runningCalls(), tag);
    }

    /**
     * 获得新的自定义客户端
     */
    private static OkHttpClient getNewClient(IBuilderSetter setter) {
        OkHttpClient.Builder builder = DEFAULT_CLIENT.newBuilder();
        setter.onSetup(builder);
        return builder.build();
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

    /**
     * 获得自定义的客户端
     */
    public OkHttpClient getClient(IBuilderSetter setter) {
        return getNewClient(setter);
    }

    /**
     * 建造设置器
     */
    public interface IBuilderSetter {
        void onSetup(OkHttpClient.Builder builder);
    }
}
