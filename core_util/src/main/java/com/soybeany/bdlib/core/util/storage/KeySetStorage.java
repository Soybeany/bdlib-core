package com.soybeany.bdlib.core.util.storage;

import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Key-Set存储器
 * <br>Created by Soybeany on 2019/4/13.
 */
public class KeySetStorage<Key, Value> extends KeyValueStorage<Key, Set<Value>> {
    private final Set<IKeyRemoveListener<Key>> mRemoveListeners = new ConcurrentHashSet<>();
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

/**
 * 并发Set
 */
class ConcurrentHashSet<T> extends AbstractSet<T> implements Set<T> {
    private static final Object PRESENT = new Object();

    private ConcurrentMap<T, Object> mMap = new ConcurrentHashMap<>();

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object o) {
        return mMap.containsKey(o);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Iterator<T> iterator() {
        return mMap.keySet().iterator();
    }

    @Override
    public boolean add(T t) {
        return mMap.putIfAbsent(t, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return mMap.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        mMap.clear();
    }
}