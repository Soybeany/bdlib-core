package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
public class OkHttpNotifierUtils {

    public static ClientPart newClient() {
        return new ClientPart();
    }

    public static class ClientPart extends OkHttpUtils.ClientPart {
        public <N extends Notifier> RequestPart<N> newNotifierRequest() {
            return new RequestPart<>(newClient());
        }
    }

    public static class RequestPart<N extends Notifier> extends OkHttpUtils.RequestPart {
        public RequestPart(OkHttpClient client) {
            super(client);
        }

        public Call newCall(IRequestGetter rGetter) {
            return newCall(rGetter, null);
        }

        public Call newCall(IRequestGetter rGetter, IConnectorSetter<N> cSetter) {
            RequestNotifier notifier = rGetter.getNewNotifier();
            // 若配置了连接器设置，则进行设置
            if (null != cSetter) {
                RequestConnector<N> connector = new RequestConnector<>();
                connector.connectN1(notifier);
                connector.connectN2(cSetter.getNewNotifier(), cSetter.getDMClass());
                cSetter.onSetupLogic(connector);
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
        N getNewNotifier();

        Class<? extends INotifyMsg> getDMClass();

        void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, N> applier);
    }
}
