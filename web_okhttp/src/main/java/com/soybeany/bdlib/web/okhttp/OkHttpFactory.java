package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.Data;
import com.soybeany.bdlib.web.core.request.IRequest;
import com.soybeany.bdlib.web.core.request.IRequestFactory;
import com.soybeany.bdlib.web.core.request.URLParser;

import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class OkHttpFactory extends IRequestFactory.Std<OkHttpRequest> {
    private static final long DEFAULT_TIMEOUT = 30;

    private static OkHttpClient DEFAULT_CLIENT = getNewOrSetupBuilder(null, builder -> OkHttpRequest.setupTimeout(builder, DEFAULT_TIMEOUT)).build();

    private OkHttpClient.Builder mClient = getNewOrSetupBuilder(DEFAULT_CLIENT.newBuilder(), null);

    /**
     * 设置后再调用{@link #getNew()}获取新factory才能应用新配置
     */
    public static void setupDefaultClient(IOkHttpEditor editor) {
        DEFAULT_CLIENT = getNewOrSetupBuilder(DEFAULT_CLIENT.newBuilder(), editor).build();
    }

    public static OkHttpFactory getNew() {
        return new OkHttpFactory();
    }

    private static OkHttpClient.Builder getNewOrSetupBuilder(OkHttpClient.Builder prototype, IOkHttpEditor editor) {
        OkHttpClient.Builder builder = Optional.ofNullable(prototype).orElseGet(OkHttpClient.Builder::new);
        Optional.ofNullable(editor).ifPresent(e -> e.onEdit(builder));
        return builder;
    }

    @Override
    public OkHttpRequest build(Data data) {
        return getNewRequestByMethod(data).setup(mClient, data, preprocessors());
    }

    @Override
    public void cancel(Object tag) {
        Dispatcher dispatcher = DEFAULT_CLIENT.dispatcher();
        cancelCalls(dispatcher.queuedCalls(), tag);
        cancelCalls(dispatcher.runningCalls(), tag);
    }

    protected OkHttpRequest getNewRequestByMethod(Data data) {
        switch (data.method) {
            case Data.METHOD_GET:
                return new GetRequest();
            case Data.METHOD_POST:
                return getNewRequestByPostType(data);
            default:
                throw new RuntimeException("使用了不支持的请求方法");
        }
    }

    protected OkHttpRequest getNewRequestByPostType(Data data) {
        switch (data.postType) {
            case Data.POST_TYPE_FORM:
                return new PostFormRequest();
            default:
                throw new RuntimeException("使用了不支持的Post类型");
        }
    }

    private void cancelCalls(List<Call> calls, Object tag) {
        for (Call call : calls) {
            Optional.ofNullable((OkHttpRequest) call.request().tag()).ifPresent(request -> {
                if (request.getRequestTag().equals(tag)) {
                    request.cancel();
                }
            });
        }
    }

    private static class GetRequest extends OkHttpRequest {
        @Override
        protected void onSetupRequest(Request.Builder builder, Data data) {
            builder.url(URLParser.mergeUrl(data.url, data.params));
        }
    }

    private abstract static class PostRequest extends OkHttpRequest {
        private IProgressListener mUploadListener;

        @Override
        protected void onSetupRequest(Request.Builder builder, Data data) {
            RequestBody body = getRequestBody(data);
            builder.post(null != mUploadListener ? new CountingRequestBody(body, mUploadListener) : body);
        }

        @Override
        public IRequest uploadListener(IProgressListener listener) {
            mUploadListener = listener;
            return this;
        }

        protected abstract RequestBody getRequestBody(Data data);
    }

    private static class PostFormRequest extends PostRequest {
        @Override
        protected RequestBody getRequestBody(Data data) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : data.params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            return formBuilder.build();
        }
    }

    public interface IOkHttpEditor {
        void onEdit(OkHttpClient.Builder builder);
    }
}
