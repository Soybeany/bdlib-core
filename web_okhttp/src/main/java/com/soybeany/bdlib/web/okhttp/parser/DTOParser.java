package com.soybeany.bdlib.web.okhttp.parser;

import com.google.gson.Gson;
import com.soybeany.bdlib.web.okhttp.core.HandledException;
import com.soybeany.bdlib.web.okhttp.dto.IDto;

/**
 *
 * <br>Created by Soybeany on 2019/3/7.
 */
public abstract class DTOParser<Result, Dto extends IDto> extends GsonParser<Result> {
    private Class<? extends Dto> mDTOClazz;

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
     * 标准实现
     */
    public static class Std<T extends IDto> extends DTOParser<T, T> {
        public Std(Class<? extends T> clazz) {
            super(clazz);
        }

        @Override
        protected T onParseDTO(Gson gson, T dto) {
            return dto;
        }
    }
}
