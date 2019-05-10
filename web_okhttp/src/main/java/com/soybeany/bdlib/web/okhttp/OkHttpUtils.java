package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;
import com.soybeany.bdlib.web.okhttp.part.IClientPart;
import com.soybeany.bdlib.web.okhttp.part.IRequestPart;

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

    public static NotifyClientPart newNotifierClient(Notifier notifier) {
        return new NotifyClientPart(notifier);
    }

    // //////////////////////////////////标准部分//////////////////////////////////

    public static class DefaultClientPart implements IClientPart<IRequestPart> {
        private IClientPart.Delegate<IRequestPart> mDelegate = new IClientPart.Delegate<>();

        @Override
        public IClientPart<IRequestPart> addSetter(OkHttpClientFactory.IClientSetter setter) {
            mDelegate.addSetter(setter);
            return this;
        }

        @Override
        public IClientPart<IRequestPart> removeSetter(OkHttpClientFactory.IClientSetter setter) {
            mDelegate.removeSetter(setter);
            return this;
        }

        @Override
        public IRequestPart newRequest() {
            return new DefaultRequestPart(newClient());
        }

        protected OkHttpClient newClient() {
            return OkHttpClientFactory.getNewClient(mDelegate.getSetter());
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

    // //////////////////////////////////带通知部分//////////////////////////////////

    public static class NotifyClientPart extends DefaultClientPart {
        private Notifier mNotifier;

        public NotifyClientPart(Notifier notifier) {
            mNotifier = notifier;
        }

        @Override
        public IRequestPart newRequest() {
            return new NotifyRequestPart(newClient(), mNotifier);
        }
    }

    public static class NotifyRequestPart extends DefaultRequestPart {
        private Notifier mNotifier;

        public NotifyRequestPart(OkHttpClient client, Notifier notifier) {
            super(client);
            mNotifier = notifier;
        }

        @Override
        public NotifyCall newCall(Request request) {
            return new NotifyCall(super.newCall(request), mNotifier);
        }
    }
}
