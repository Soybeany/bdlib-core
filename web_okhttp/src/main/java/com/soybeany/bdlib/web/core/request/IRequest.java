package com.soybeany.bdlib.web.core.request;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.Data;
import com.soybeany.bdlib.web.core.ICallback;
import com.soybeany.bdlib.web.core.parser.IParser;

import java.util.List;

/**
 * 请求的封装
 * <br>Created by Soybeany on 2019/2/26.
 */
public interface IRequest {

    /**
     * 设置额外数据
     */
    @SuppressWarnings("UnusedReturnValue")
    IRequest addExData(String key, Object value);

    /**
     * 设置超时
     */
    @SuppressWarnings("UnusedReturnValue")
    IRequest timeout(long sec);

    /**
     * 设置上传监听器(部分请求方式可能不支持)
     */
    default IRequest uploadListener(IProgressListener listener) {
        throw new RuntimeException("此请求方式不支持上传监听");
    }

    /**
     * 设置下载监听器(部分请求方式可能不支持)
     */
    default IRequest downloadListener(IProgressListener listener) {
        throw new RuntimeException("此请求方式不支持下载监听");
    }

    /**
     * 异步请求
     */
    <Result> void enqueue(IParser<Result> parser, List<ICallback<Result>> callbacks);

    /**
     * 同步请求
     */
    <Result> void sync(IParser<Result> parser, List<ICallback<Result>> callbacks);

    /**
     * 直接执行
     */
    IResponse execute() throws Exception;

    /**
     * 直接执行(带解析)
     */
    default <Result> Result execute(IParser<Result> parser) throws Exception {
        return parser.parse(execute(), null);
    }

    /**
     * 取消请求
     */
    void cancel();

    class Wrapper implements IRequest {
        private IRequest mTarget;
        private Data mData;

        public Wrapper(IRequest target, Data data) {
            mTarget = target;
            mData = data;
        }

        @Override
        public IRequest addExData(String key, Object value) {
            mTarget.addExData(key, value);
            return this;
        }

        @Override
        public IRequest timeout(long sec) {
            mTarget.timeout(sec);
            return this;
        }

        @Override
        public IRequest uploadListener(IProgressListener listener) {
            mTarget.uploadListener(listener);
            return this;
        }

        @Override
        public IRequest downloadListener(IProgressListener listener) {
            mTarget.downloadListener(listener);
            return this;
        }

        @Override
        public <Result> void enqueue(IParser<Result> parser, List<ICallback<Result>> callbacks) {
            mTarget.enqueue(parser, callbacks);
        }

        @Override
        public <Result> void sync(IParser<Result> parser, List<ICallback<Result>> callbacks) {
            mTarget.sync(parser, callbacks);
        }

        @Override
        public IResponse execute() throws Exception {
            return mTarget.execute();
        }

        @Override
        public void cancel() {
            mTarget.cancel();
        }

        protected Data getData() {
            return mData;
        }
    }
}
