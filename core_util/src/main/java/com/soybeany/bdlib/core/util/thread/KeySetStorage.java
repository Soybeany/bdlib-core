package com.soybeany.bdlib.core.util.thread;

import com.soybeany.bdlib.core.java8.function.Consumer;

import java.util.HashSet;
import java.util.Set;

/**
 * Key-Set存储器
 * <br>Created by Soybeany on 2019/4/13.
 */
public class KeySetStorage<Key, Value> extends KeyValueStorage<Key, Set<Value>> {
    private final ISetProvider<Value> mProvider;

    public KeySetStorage() {
        this(HashSet::new);
    }

    public KeySetStorage(ISetProvider<Value> provider) {
        mProvider = provider;
    }

    public void putVal(Key key, Value value) {
        Set<Value> set = get(key);
        if (null == set) {
            putIfAbsent(key, mProvider.getNewSet());
            set = get(key);
        }
        set.add(value);
    }

    public void removeVal(Key key, Value value) {
        Set<Value> set = get(key);
        set.remove(value);
        if (set.isEmpty()) {
            remove(key);
        }
    }

    public boolean invokeEach(Key key, Consumer<Value> consumer) {
        return invoke(key, set -> {
            for (Value value : set) {
                consumer.accept(value);
            }
        });
    }

    public interface ISetProvider<Value> {
        Set<Value> getNewSet();
    }
}
