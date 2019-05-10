package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IClientPart<RequestPart extends IRequestPart> {
    IClientPart<RequestPart> addSetter(OkHttpClientFactory.IClientSetter setter);

    IClientPart<RequestPart> removeSetter(OkHttpClientFactory.IClientSetter setter);

    RequestPart newRequest();

    class Delegate<RequestPart extends IRequestPart> implements IClientPart<RequestPart> {
        private final Set<OkHttpClientFactory.IClientSetter> mOuterSetters = new LinkedHashSet<>();
        private final OkHttpClientFactory.IClientSetter mSetter = builder -> IterableUtils.forEach(mOuterSetters, (setter, flag) -> setter.onSetup(builder));

        @Override
        public IClientPart<RequestPart> addSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.add(setter);
            return this;
        }

        @Override
        public IClientPart<RequestPart> removeSetter(OkHttpClientFactory.IClientSetter setter) {
            mOuterSetters.remove(setter);
            return this;
        }

        @Override
        public RequestPart newRequest() {
            throw new RuntimeException("请勿调用此方法");
        }

        public OkHttpClientFactory.IClientSetter getSetter() {
            return mSetter;
        }
    }

}
