package com.soybeany.bdlib.log.extract.model;

/**
 * <br>Created by Soybeany on 2019/5/31.
 */
public interface IDataProvider<Item, Data> {
    Data getData(Item item);
}
