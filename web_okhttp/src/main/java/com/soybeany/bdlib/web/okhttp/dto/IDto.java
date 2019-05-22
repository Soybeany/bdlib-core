package com.soybeany.bdlib.web.okhttp.dto;

import java.io.Serializable;

/**
 * <br>Created by Soybeany on 2019/5/22.
 */
public interface IDto extends Serializable {
    boolean isNorm();

    String errMsg();
}
