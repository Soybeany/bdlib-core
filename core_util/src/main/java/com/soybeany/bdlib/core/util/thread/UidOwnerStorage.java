package com.soybeany.bdlib.core.util.thread;

import com.soybeany.bdlib.core.java8.function.Consumer;

import static com.soybeany.bdlib.core.util.thread.UidOwnerStorage.IUidOwner.getKey;

/**
 * <br>Created by Soybeany on 2019/4/12.
 */
public class UidOwnerStorage {

    private KeyValueStorage mStorage = new KeyValueStorage();

    public void put(IUidOwner owner) {
        mStorage.put(getKey(owner.getClass(), owner.getUid()), owner);
    }

    public <Owner extends IUidOwner> Owner get(Class<Owner> clazz) {
        return get(clazz, IUidOwner.DEFAULT_UID);
    }

    @SuppressWarnings("unchecked")
    public <Owner extends IUidOwner> Owner get(Class<Owner> clazz, String uid) {
        return (Owner) mStorage.get(getKey(clazz, uid));
    }

    public <Owner extends IUidOwner> boolean invoke(Class<Owner> clazz, Consumer<Owner> consumer) {
        return invoke(clazz, IUidOwner.DEFAULT_UID, consumer);
    }

    public <Owner extends IUidOwner> boolean invoke(Class<Owner> clazz, String uid, Consumer<Owner> consumer) {
        return mStorage.invoke(getKey(clazz, uid), consumer);
    }

    public <Owner extends IUidOwner> Owner remove(Class<Owner> clazz) {
        return remove(clazz, IUidOwner.DEFAULT_UID);
    }

    @SuppressWarnings("unchecked")
    public <Owner extends IUidOwner> Owner remove(Class<Owner> clazz, String uid) {
        return (Owner) mStorage.remove(getKey(clazz, uid));
    }

    public void clear() {
        mStorage.clear();
    }

    public interface IUidOwner {
        String DEFAULT_UID = "default_uid";

        static String getKey(Class clazz, String uid) {
            return clazz.getName() + "-" + uid;
        }

        /**
         * 用于相同类型时区分当前对象
         */
        default String getUid() {
            return DEFAULT_UID;
        }
    }
}
