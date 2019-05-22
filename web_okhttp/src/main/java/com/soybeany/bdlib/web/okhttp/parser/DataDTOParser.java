package com.soybeany.bdlib.web.okhttp.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soybeany.bdlib.web.okhttp.dto.IDataDto;

/**
 * 常规使用:项目中继承{@link GsonParser.Std}或{@link Generic}，然后在构造器中写项目的DTO，外部只需提供一个参数就行
 * <br>Created by Soybeany on 2019/5/22.
 */
public abstract class DataDTOParser<T> extends DTOParser<T, IDataDto<T>> {

    public DataDTOParser(Class<? extends IDataDto<T>> clazz) {
        super(clazz);
    }

    @Override
    protected T onParseDTO(Gson gson, IDataDto<T> dto) {
        return onParseDTO(gson, gson.toJson(dto.data()));
    }

    protected abstract T onParseDTO(Gson gson, String json);

    /**
     * 标准实现(使用DataDto)
     * <br>抽象类是为了在项目使用时进行提醒，要使用继承来简化DTO输入
     */
    public abstract static class Std<T> extends DataDTOParser<T> {
        private Class<T> mClazz;

        public Std(Class<? extends IDataDto<T>> dtoClazz, Class<T> clazz) {
            super(dtoClazz);
            mClazz = clazz;
        }

        @Override
        protected T onParseDTO(Gson gson, String json) {
            return gson.fromJson(json, mClazz);
        }
    }

    /**
     * 泛型实现(使用DataDto)
     */
    public abstract static class Generic<T> extends DataDTOParser<T> {
        private TypeToken<T> mToken;

        public Generic(Class<? extends IDataDto<T>> dtoClazz, TypeToken<T> token) {
            super(dtoClazz);
            mToken = token;
        }

        @Override
        protected T onParseDTO(Gson gson, String json) {
            return gson.fromJson(json, mToken.getType());
        }
    }
}
