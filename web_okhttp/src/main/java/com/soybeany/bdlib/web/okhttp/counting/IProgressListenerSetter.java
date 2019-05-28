package com.soybeany.bdlib.web.okhttp.counting;

import com.soybeany.bdlib.core.util.file.IProgressListener;

import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/5/28.
 */
public interface IProgressListenerSetter {
    void onSetup(Set<IProgressListener> listeners);

    interface IApplier {
        IApplier listeners(IProgressListenerSetter setter);
    }
}
