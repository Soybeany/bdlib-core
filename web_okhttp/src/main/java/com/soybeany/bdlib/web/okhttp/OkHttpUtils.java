package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.part.DefaultCall;
import com.soybeany.bdlib.web.okhttp.part.IClientPart;
import com.soybeany.bdlib.web.okhttp.part.IRequestPart;
import com.soybeany.bdlib.web.okhttp.part.IStateListener;

import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

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
        private final List<IStateListener> mListeners = new LinkedList<>();

        public DefaultRequestPart(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public DefaultCall newCall(Request request) {
            return new DefaultCall(mClient.newCall(request), mListeners);
        }

        public DefaultRequestPart addStateListener(IStateListener listener) {
            mListeners.add(listener);
            return this;
        }

        public DefaultRequestPart removeStateListener(IStateListener listener) {
            mListeners.remove(listener);
            return this;
        }
    }
}
