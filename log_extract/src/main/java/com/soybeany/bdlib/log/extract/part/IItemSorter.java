package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.IItem;

import java.util.List;

/**
 * <br>Created by Soybeany on 2019/6/6.
 */
public interface IItemSorter<Item extends IItem> {

    void onSort(List<Item> items);
}
