package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public interface INotifier {

    void addReceiver(NotifyReceiver receiver);

    void removeReceiver(NotifyReceiver receiver);

    String getNotifyKey();

    void registerReceivers();

    void unregisterReceivers();

    class Impl implements INotifier {
        private Set<NotifyReceiver> mReceivers = new HashSet<>();
        private String mNotifyKey;

        public Impl(String notifyKey) {
            mNotifyKey = notifyKey;
        }

        @Override
        public void addReceiver(NotifyReceiver receiver) {
            mReceivers.add(receiver.withNotifyKey(mNotifyKey));
        }

        @Override
        public void removeReceiver(NotifyReceiver receiver) {
            mReceivers.remove(receiver);
        }

        @Override
        public String getNotifyKey() {
            return mNotifyKey;
        }

        @Override
        public void registerReceivers() {
            IterableUtils.forEach(mReceivers, (receiver, flag) -> NotifyUtils.register(mNotifyKey, receiver));
        }

        @Override
        public void unregisterReceivers() {
            IterableUtils.forEach(mReceivers, (receiver, flag) -> NotifyUtils.unregister(receiver));
        }
    }
}
