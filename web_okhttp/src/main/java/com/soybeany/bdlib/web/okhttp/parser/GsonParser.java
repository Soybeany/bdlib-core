package com.soybeany.bdlib.web.okhttp.parser;

import com.google.gson.Gson;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;

import java.util.HashSet;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/3/7.
 */
public abstract class GsonParser<T> implements IParser<T> {
    private static final Gson DEFAULT_GSON = new Gson();

    private Gson mGson = DEFAULT_GSON;
    private final Set<Consumer<String>> mJsonWatchers = new HashSet<>();

    public GsonParser<T> gson(Gson gson) {
        Optional.ofNullable(gson).ifPresent(g -> mGson = g);
        return this;
    }

    @Override
    public T parse(ResponseBody body, IProgressListener listener) throws Exception {
        String json = StringParser.get().parse(body, listener);
        IterableUtils.forEach(mJsonWatchers, (watcher, flag) -> watcher.accept(json));
        return onParse(mGson, json);
    }

    /**
     * 添加Json观察者，观察解析后的json
     */
    public GsonParser<T> addJsonWatcher(Consumer<String> watcher) {
        mJsonWatchers.add(watcher);
        return this;
    }

    /**
     * 获得全部Json观察者
     */
    public Set<Consumer<String>> jsonWatchers() {
        return mJsonWatchers;
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
