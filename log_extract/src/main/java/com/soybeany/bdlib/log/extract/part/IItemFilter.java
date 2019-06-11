package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.IItem;

/**
 * <br>Created by Soybeany on 2019/6/6.
 */
public interface IItemFilter<Item extends IItem> {

    boolean onFilter(Item item);

}
