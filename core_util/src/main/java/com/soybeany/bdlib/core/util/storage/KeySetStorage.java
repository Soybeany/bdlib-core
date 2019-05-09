package com.soybeany.bdlib.core.util.storage;

import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Key-Set存储器
 * <br>Created by Soybeany on 2019/4/13.
 */
public class KeySetStorage<Key, Value> extends KeyValueStorage<Key, Set<Value>> {
    private final Set<IKeyRemoveListener<Key>> mRemoveListeners = new HashSet<>();
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
        if (null == set) {
            return;
        }
        set.remove(value);
        if (set.isEmpty()) {
            remove(key);
            IterableUtils.forEach(mRemoveListeners, (listener, flag) -> listener.onRemoved(key));
        }
    }

    public boolean containVal(Key key, Value value) {
        if (!containKey(key)) {
            return false;
        }
        return get(key).contains(value);
    }

    public boolean invokeVal(Key key, Consumer<Value> consumer) {
        return invoke(key, set -> innerInvoke(set, consumer));
    }

    public void invokeAllVal(Consumer<Value> consumer) {
        invokeAll(set -> innerInvoke(set, consumer));
    }

    public void addKeyRemoveListener(IKeyRemoveListener<Key> listener) {
        mRemoveListeners.add(listener);
    }

    public void removeKeyRemoveListener(IKeyRemoveListener<Key> listener) {
        mRemoveListeners.remove(listener);
    }

    private void innerInvoke(Set<Value> set, Consumer<Value> consumer) {
        for (Value value : set) {
            consumer.accept(value);
        }
    }

    public interface ISetProvider<Value> {
        Set<Value> getNewSet();
    }

    /**
     * 键被移除的监听
     */
    public interface IKeyRemoveListener<Key> {
        void onRemoved(Key key);
    }
}
