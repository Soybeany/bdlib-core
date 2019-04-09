package com.soybeany.bdlib.web.core.request;

import com.soybeany.bdlib.web.core.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public interface IRequestFactory<Request extends IRequest> {

    @SuppressWarnings("UnusedReturnValue")
    IRequestFactory<Request> addPreprocessor(IPreprocessor processor);

    List<IPreprocessor> preprocessors();

    Request build(Data data);

    void cancel(Object tag);

    abstract class Std<Request extends IRequest> implements IRequestFactory<Request> {
        private final List<IPreprocessor> mPreprocessors = new LinkedList<>();

        @Override
        public IRequestFactory<Request> addPreprocessor(IPreprocessor processor) {
            if (null != processor) {
                mPreprocessors.add(processor);
            }
            return this;
        }

        @Override
        public List<IPreprocessor> preprocessors() {
            return mPreprocessors;
        }
    }

    abstract class Wrapper<W extends IRequest, R extends IRequest> implements IRequestFactory<W> {
        protected IRequestFactory<R> mFactory;

        public Wrapper(IRequestFactory<R> factory) {
            mFactory = factory;
        }

        @Override
        public IRequestFactory<W> addPreprocessor(IPreprocessor listener) {
            mFactory.addPreprocessor(listener);
            return this;
        }

        @Override
        public List<IPreprocessor> preprocessors() {
            return mFactory.preprocessors();
        }

        @Override
        public void cancel(Object tag) {
            mFactory.cancel(tag);
        }
    }
}
