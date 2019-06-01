package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

/**
 * <br>Created by Soybeany on 2019/5/31.
 */
public class KeyFilter<Item> extends StringFilter<Item> {
    public KeyFilter(String data, IDataProvider<Item, String> provider) {
        super(data, provider);
    }


}
