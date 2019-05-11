package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;

import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 工具栏入口
 * <br>Created by Soybeany on 2019/4/11.
 */
public class OkHttpUtils {

    public static ClientPart newDefaultClient() {
        return new ClientPart();
    }

    public static class ClientPart {
        private final Set<OkHttpClientFactory.IClientSetter> mOuterSetters = new LinkedHashSet<>();
        private final OkHttpClientFactory.IClientSetter mSetter = builder -> IterableUtils.forEach(mOuterSetters, (setter, flag) -> setter.onSetup(builder));
        private Notifier mNotifier;

        public ClientPart addSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.add(setter);
            return this;
        }

        public ClientPart removeSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.remove(setter);
            return this;
        }

        public ClientPart notifier(Notifier notifier) {
            mNotifier = notifier;
            return this;
        }

        public RequestPart newRequest() {
            return new RequestPart(newClient(), mNotifier);
        }

        protected OkHttpClient newClient() {
            return OkHttpClientFactory.getNewClient(mSetter);
        }
    }

    public static class RequestPart {
        private OkHttpClient mClient;
        private Notifier mNotifier;

        public RequestPart(OkHttpClient client, Notifier notifier) {
            mClient = client;
            mNotifier = notifier;
        }

        public NotifyCall newCall(RequestGetter getter) {
            return new NotifyCall(mClient.newCall(getter.getRequest(mNotifier)), mNotifier);
        }
    }

    public interface RequestGetter {
        Request getRequest(Notifier notifier);
    }
}
