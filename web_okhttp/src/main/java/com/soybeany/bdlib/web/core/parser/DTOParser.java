package com.soybeany.bdlib.web.core.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soybeany.bdlib.web.core.HandledException;

import java.io.Serializable;

/**
 * <br>Created by Soybeany on 2019/3/7.
 */
public abstract class DTOParser<T> extends GsonParser<T> {
    private Class<? extends IStruct> mDTOClazz;

    public DTOParser(Class<? extends IStruct> clazz) {
        mDTOClazz = clazz;
    }

    @Override
    protected T onParse(Gson gson, String json) {
        IStruct dto = gson.fromJson(json, mDTOClazz);
        if (dto.isNorm()) {
            return onParseDTO(gson, gson.toJson(dto.data()));
        } else {
            throw new HandledException(onParseDTOErrMsg(dto.errMsg()));
        }
    }

    protected abstract T onParseDTO(Gson gson, String json);

    protected String onParseDTOErrMsg(String errMsg) {
        return errMsg + "[服务器]";
    }

    public abstract static class Std<T> extends DTOParser<T> {
        private Class<T> mClazz;

        public Std(Class<? extends IStruct> dtoClazz, Class<T> clazz) {
            super(dtoClazz);
            mClazz = clazz;
        }

        @Override
        protected T onParseDTO(Gson gson, String json) {
            return gson.fromJson(json, mClazz);
        }
    }

    public abstract static class Generic<T> extends DTOParser<T> {

        private TypeToken<T> mToken;

        public Generic(Class<? extends IStruct> dtoClazz, TypeToken<T> token) {
            super(dtoClazz);
            mToken = token;
        }

        @Override
        protected T onParseDTO(Gson gson, String json) {
            return gson.fromJson(json, mToken.getType());
        }
    }

    public interface IStruct<T> extends Serializable {

        boolean isNorm();

        T data();

        String errMsg();
    }
}
