package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.core.util.notify.NotifierConnector;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
public class RequestConnector<N extends Notifier> extends NotifierConnector<RequestNotifier, N> {
    public void connectN1(RequestNotifier notifier) {
        super.connectN1(notifier, RequestMsg.OnFinish.class);
    }
}
