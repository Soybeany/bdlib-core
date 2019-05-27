package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;
import com.soybeany.bdlib.web.okhttp.parser.IParser;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
public class NotifierCallback<Result> extends OkHttpCallback<Result> {
    private RequestNotifier mNotifier;

    public NotifierCallback(IParser<Result> parser) {
        super(parser);
    }

    @Override
    protected CountingResponseBody getNewCountResponseBody(ResponseBody body, List<IProgressListener> listeners) {
        return new NotifierCountingBody.Response(body, listeners, mNotifier);
    }

    public void setNotifier(RequestNotifier notifier) {
        mNotifier = notifier;
    }
}
