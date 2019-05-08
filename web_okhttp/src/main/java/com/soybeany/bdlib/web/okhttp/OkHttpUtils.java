package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.web.okhttp.core.NotifyRequest;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.part.DefaultCall;
import com.soybeany.bdlib.web.okhttp.part.IClientPart;
import com.soybeany.bdlib.web.okhttp.part.IRequestPart;

import okhttp3.OkHttpClient;

/**
 * 工具栏入口
 * <br>Created by Soybeany on 2019/4/11.
 */
public class OkHttpUtils {

    public static DefaultClientPart newClient() {
        return new DefaultClientPart();
    }

    public static class DefaultClientPart implements IClientPart<DefaultRequestPart> {
        @Override
        public DefaultRequestPart newRequest(OkHttpClientFactory.IClientSetter setter) {
            return new DefaultRequestPart(OkHttpClientFactory.getNewClient(setter));
        }
    }

    public static class DefaultRequestPart implements IRequestPart {
        private OkHttpClient mClient;

        public DefaultRequestPart(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public DefaultCall newCall(NotifyRequest notifyRequest) {
            return new DefaultCall(mClient.newCall(notifyRequest.request), notifyRequest.key);
        }
    }
}
