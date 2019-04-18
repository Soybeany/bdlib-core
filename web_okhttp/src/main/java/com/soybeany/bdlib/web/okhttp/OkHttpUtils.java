package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.part.IClientPart;
import com.soybeany.bdlib.web.okhttp.part.IRequestPart;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
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
        public Call newCall(Request request) {
            return mClient.newCall(request);
        }
    }
}
