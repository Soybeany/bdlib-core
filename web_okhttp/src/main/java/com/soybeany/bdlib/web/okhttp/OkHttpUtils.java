package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.web.okhttp.core.NotifyRequest;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;
import com.soybeany.bdlib.web.okhttp.part.IClientPart;
import com.soybeany.bdlib.web.okhttp.part.IRequestPart;

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

    public static DefaultClientPart newDefaultClient() {
        return new DefaultClientPart();
    }

    public static NotifyClientPart newNotifierClient() {
        return new NotifyClientPart();
    }

    // //////////////////////////////////标准部分//////////////////////////////////

    public static class DefaultClientPart implements IClientPart<IRequestPart> {
        private final Set<OkHttpClientFactory.IClientSetter> mOuterSetters = new LinkedHashSet<>();
        protected final OkHttpClientFactory.IClientSetter mSetter = builder -> IterableUtils.forEach(mOuterSetters, (setter, flag) -> setter.onSetup(builder));

        @Override
        public IClientPart<IRequestPart> addSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.add(setter);
            return this;
        }

        @Override
        public IClientPart<IRequestPart> removeSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.remove(setter);
            return this;
        }

        @Override
        public IRequestPart newRequest() {
            return new DefaultRequestPart(OkHttpClientFactory.getNewClient(mSetter));
        }
    }

    public static class DefaultRequestPart implements IRequestPart<Request> {
        private OkHttpClient mClient;

        public DefaultRequestPart(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public Call newCall(Request request) {
            return mClient.newCall(request);
        }
    }

    // //////////////////////////////////带通知部分//////////////////////////////////

    public static class NotifyClientPart extends DefaultClientPart {
        @Override
        public IRequestPart newRequest() {
            return new NotifyRequestPart(OkHttpClientFactory.getNewClient(mSetter));
        }
    }

    public static class NotifyRequestPart implements IRequestPart<NotifyRequest> {
        private OkHttpClient mClient;

        public NotifyRequestPart(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public NotifyCall newCall(NotifyRequest notifyRequest) {
            return new NotifyCall(mClient.newCall(notifyRequest.request), notifyRequest.key);
        }
    }
}
