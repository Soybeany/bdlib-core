package com.soybeany.bdlib.web.okhttp.parser;

import com.google.gson.Gson;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.IProgressListener;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/3/7.
 */
public abstract class GsonParser<T> implements IParser<T> {
    private static final Gson DEFAULT_GSON = new Gson();

    private Gson mGson = DEFAULT_GSON;

    public GsonParser<T> gson(Gson gson) {
        Optional.ofNullable(gson).ifPresent(g -> mGson = g);
        return this;
    }

    @Override
    public T parse(ResponseBody body, IProgressListener listener) throws Exception {
        return onParse(mGson, StringParser.get().parse(body, null));
    }

    protected abstract T onParse(Gson gson, String json) throws Exception;

    public static class Std<T> extends GsonParser<T> {
        private Class<T> mClazz;

        public Std(Class<T> clazz) {
            mClazz = clazz;
        }

        @Override
        protected T onParse(Gson gson, String json) {
            return gson.fromJson(json, mClazz);
        }
    }
}