package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Notifier版工具类入口
 * <br>Created by Soybeany on 2019/5/26.
 */
public class OkHttpNotifierUtils {

    public static <N extends Notifier> ClientPart<N> newClient(Class<N> clazz) {
        return new ClientPart<>();
    }

    public static class ClientPart<N extends Notifier> extends OkHttpUtils.ClientPart {
        private IConnectorSetter<N> mCSetter;

        @Override
        public ClientPart addSetter(OkHttpClientFactory.IClientSetter setter) {
            super.addSetter(setter);
            return this;
        }

        @Override
        public ClientPart removeSetter(OkHttpClientFactory.IClientSetter setter) {
            super.removeSetter(setter);
            return this;
        }

        public ClientPart connector(IConnectorSetter<N> cSetter) {
            mCSetter = cSetter;
            return this;
        }

        public RequestPart<N> newNotifierRequest() {
            return new RequestPart<>(newClient(), mCSetter);
        }
    }

    public static class RequestPart<N extends Notifier> extends OkHttpUtils.RequestPart {
        private IConnectorSetter<N> mCSetter;

        public RequestPart(OkHttpClient client, IConnectorSetter<N> cSetter) {
            super(client);
            mCSetter = cSetter;
        }

        public Call newCall(IRequestGetter rGetter) {
            RequestNotifier notifier = rGetter.getNewNotifier();
            // 若配置了连接器设置，则进行设置
            if (null != mCSetter) {
                RequestConnector<N> connector = new RequestConnector<>();
                connector.connectN1(notifier);
                connector.connectN2(mCSetter.getNewNotifier(), mCSetter.getDMClass());
                mCSetter.onSetupLogic(connector);
            }
            return new NotifierCall(newCall(rGetter.getRequest(notifier)), notifier);
        }
    }

    public interface IRequestGetter {
        Request getRequest(RequestNotifier notifier);

        default RequestNotifier getNewNotifier() {
            return new RequestNotifier();
        }
    }

    public interface IConnectorSetter<N extends Notifier> {
        static <Invoke extends INotifyMsg.IInvoker, N extends Notifier<Invoke, ?>> void invoke(N notifier, Invoke msg) {
            if (null != notifier) {
                Notifier.Invoker<Invoke> invoker = notifier.invoker();
                invoker.notifyNow(msg);
            }
        }

        N getNewNotifier();

        Class<? extends INotifyMsg> getDMClass();

        void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, N> applier);
    }
}
