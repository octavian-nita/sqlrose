package net.appfold.sqlrose.cache;

import java.util.concurrent.locks.*;
import java.util.function.Function;

/**
 * A synchronized version of {@link SoftCache}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 20, 2019
 */
public class ConcurrentSoftCache<K, V> extends SoftCache<K, V> {

    protected final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ConcurrentSoftCache() {}

    public ConcurrentSoftCache(int maxSize) { super(maxSize); }

    @Override
    public V getOrCompute(K key, Function<? super K, ? extends V> computation) {
        rwLock.writeLock().lock();
        try {
            return super.getOrCompute(key, computation);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        rwLock.readLock().lock();
        try {
            return super.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(K key) {
        rwLock.readLock().lock();
        try {
            return super.contains(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public V remove(K key) {
        rwLock.writeLock().lock();
        try {
            return super.remove(key);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        rwLock.writeLock().lock();
        try {
            super.clear();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        rwLock.readLock().lock();
        try {
            return super.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean isFull() {
        rwLock.readLock().lock();
        try {
            return super.isFull();
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
