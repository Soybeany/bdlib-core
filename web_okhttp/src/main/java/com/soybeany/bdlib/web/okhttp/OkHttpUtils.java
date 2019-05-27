package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.web.okhttp.core.FastFailCall;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 工具栏入口
 * <br>Created by Soybeany on 2019/4/11.
 */
public class OkHttpUtils {

    public static ClientPart newClient() {
        return new ClientPart();
    }

    public static class ClientPart {
        private final Set<OkHttpClientFactory.IClientSetter> mOuterSetters = new LinkedHashSet<>();
        private final OkHttpClientFactory.IClientSetter mSetter = builder -> IterableUtils.forEach(mOuterSetters, (setter, flag) -> setter.onSetup(builder));

        public ClientPart addSetter(OkHttpClientFactory.IClientSetter setter) {
            Optional.ofNullable(setter).ifPresent(mOuterSetters::add);
            return this;
        }

        public ClientPart removeSetter(OkHttpClientFactory.IClientSetter setter) {
            Optional.ofNullable(setter).ifPresent(mOuterSetters::remove);
            return this;
        }

        public RequestPart newRequest() {
            return new RequestPart(newClient());
        }

        protected OkHttpClient newClient() {
            return OkHttpClientFactory.getNewClient(mSetter);
        }
    }

    public static class RequestPart {
        private OkHttpClient mClient;

        public RequestPart(OkHttpClient client) {
            mClient = client;
        }

        public Call newCall(Request request) {
            return new FastFailCall(getNewCall(request));
        }

        protected Call getNewCall(Request request) {
            return mClient.newCall(request);
        }
    }
}
