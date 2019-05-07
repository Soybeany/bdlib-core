package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 请求构建工厂
 * <br>Created by Soybeany on 2019/4/9.
 */
public class OkHttpRequestFactory {

    /**
     * OkHttp原版
     */
    public static Request.Builder original() {
        return new Request.Builder();
    }

    /**
     * Get
     */
    public static GetBuilder get(String url) {
        return new GetBuilder(url);
    }

    /**
     * Post表单
     */
    public static PostFormBuilder postForm(String url) {
        return new PostFormBuilder(url);
    }

    /**
     * 建造器基类
     */
    public abstract static class BasicBuilder {
        private String mUrl;
        private final Request.Builder mOriginalBuilder = new Request.Builder();

        public BasicBuilder(String url) {
            mOriginalBuilder.url(mUrl = url);
        }

        /**
         * 设置请求头
         */
        public BasicBuilder header(String key, String value) {
            mOriginalBuilder.header(key, value);
            return this;
        }

        /**
         * 设置请求标识，用于取消
         */
        public BasicBuilder tag(Object tag) {
            mOriginalBuilder.tag(tag);
            return this;
        }

        /**
         * 构造请求
         */
        public Request build() {
            onBuild(mUrl, mOriginalBuilder);
            return mOriginalBuilder.build();
        }

        protected abstract void onBuild(String url, Request.Builder builder);
    }

    /**
     * post请求基类，允许统计上传进度
     */
    public abstract static class PostBuilder extends BasicBuilder {
        private final List<IProgressListener> mUploadListeners = new LinkedList<>(); // 上传监听器

        public PostBuilder(String url) {
            super(url);
        }

        /**
         * 设置上传监听器
         */
        public PostBuilder addUploadListener(IProgressListener listener) {
            mUploadListeners.add(listener);
            return this;
        }

        /**
         * 移除上传监听器
         */
        public PostBuilder removeUploadListener(IProgressListener listener) {
            mUploadListeners.remove(listener);
            return this;
        }

        @Override
        protected void onBuild(String url, Request.Builder builder) {
            RequestBody body = getRequestBody();
            builder.post(!mUploadListeners.isEmpty() ? new CountingRequestBody(body, mUploadListeners) : body);
        }

        protected abstract RequestBody getRequestBody();
    }

    public static class GetBuilder extends BasicBuilder {
        private final Map<String, String> mParams = new HashMap<>();

        public GetBuilder(String url) {
            super(url);
        }

        @Override
        protected void onBuild(String url, Request.Builder builder) {
            builder.url(URLParser.mergeUrl(url, mParams));
        }

        /**
         * 设置键值对参数
         */
        public GetBuilder param(String key, String value) {
            mParams.put(key, value);
            return this;
        }
    }

    public static class PostFormBuilder extends PostBuilder {
        private final Map<String, String> mParams = new HashMap<>();

        public PostFormBuilder(String url) {
            super(url);
        }

        @Override
        protected RequestBody getRequestBody() {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            return formBuilder.build();
        }

        /**
         * 设置键值对参数
         */
        public PostFormBuilder param(String key, String value) {
            mParams.put(key, value);
            return this;
        }
    }
}
