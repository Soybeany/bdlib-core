package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IDataProvider;

/**
 * Key容器
 * <br>Created by Soybeany on 2019/6/1.
 */
public interface IKeyContainer<Data> {
    /**
     * 容器中是否含有key
     */
    boolean hasKey();

    /**
     * 指定目标中是否包含有key
     */
    boolean isTargetContainKey(Data target);

    abstract class KeyContainerFilter<Item, Data> extends IFilter.Impl<Item, Data> implements IKeyContainer<Data> {
        public KeyContainerFilter(IDataProvider<Item, Data> provider) {
            super(provider);
        }

        @Override
        public boolean isActive() {
            return hasKey();
        }

        @Override
        protected boolean shouldInterceptWithData(Data data) {
            return !isTargetContainKey(data);
        }
    }
}
