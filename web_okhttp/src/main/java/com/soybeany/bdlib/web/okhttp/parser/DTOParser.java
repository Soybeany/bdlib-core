package com.soybeany.bdlib.web.okhttp.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soybeany.bdlib.web.okhttp.core.HandledException;

import java.io.Serializable;

/**
 * 常规使用:项目中继承{@link Std}或{@link Generic}，然后在构造器中写项目的DTO，外部只需提供一个参数就行
 * <br>Created by Soybeany on 2019/3/7.
 */
public abstract class DTOParser<Result, Dto extends DTOParser.IDto> extends GsonParser<Result> {
    private Class<? extends Dto> mDTOClazz;

    protected static String toJson(Gson gson, IDataDto dto) {
        return gson.toJson(dto.data());
    }

    public DTOParser(Class<? extends Dto> clazz) {
        mDTOClazz = clazz;
    }

    @Override
    protected Result onParse(Gson gson, String json) throws Exception {
        Dto dto = gson.fromJson(json, mDTOClazz);
        if (dto.isNorm()) {
            return onParseDTO(gson, dto);
        } else {
            throw new HandledException(onParseDTOErrMsg(dto.errMsg()));
        }
    }

    protected abstract Result onParseDTO(Gson gson, Dto dto);

    protected String onParseDTOErrMsg(String errMsg) {
        return errMsg + "[服务器]";
    }

    /**
     * 默认的简单实现
     */
    public static class Default<T extends DTOParser.IDto> extends DTOParser<T, T> {
        public Default(Class<? extends T> clazz) {
            super(clazz);
        }

        @Override
        protected T onParseDTO(Gson gson, T dto) {
            return dto;
        }
    }

    /**
     * 标准实现(使用DataDto)
     * <br>抽象类是为了在项目使用时进行提醒，要使用继承来简化DTO输入
     */
    public abstract static class Std<T> extends DTOParser<T, IDataDto<T>> {
        private Class<T> mClazz;

        public Std(Class<? extends IDataDto<T>> dtoClazz, Class<T> clazz) {
            super(dtoClazz);
            mClazz = clazz;
        }

        @Override
        protected T onParseDTO(Gson gson, IDataDto<T> dto) {
            return gson.fromJson(toJson(gson, dto), mClazz);
        }
    }

    /**
     * 泛型实现(使用DataDto)
     */
    public abstract static class Generic<T> extends DTOParser<T, IDataDto<T>> {
        private TypeToken<T> mToken;

        public Generic(Class<? extends IDataDto<T>> dtoClazz, TypeToken<T> token) {
            super(dtoClazz);
            mToken = token;
        }

        @Override
        protected T onParseDTO(Gson gson, IDataDto<T> dto) {
            return gson.fromJson(toJson(gson, dto), mToken.getType());
        }
    }

    public interface IDto extends Serializable {
        boolean isNorm();

        String errMsg();
    }

    public interface IDataDto<T> extends IDto {
        T data();
    }
}
