package net.appfold.sqlrose.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

/**
 * A <em>poor man's</em> <strong>bounded</strong> cache with a <em>minimal interface</em> that uses a
 * {@link SoftReference}-wrapped {@link Map} instance as the backing structure to hold cached data. Whether
 * {@code null} keys are allowed or not as well as other characteristics of this instance can be customized
 * by overriding {@link #createBoundedCache(int)}.
 * <p/>
 * By default, a {@link LinkedHashMap} instance is used as the backing structure, which allows {@code null} keys and
 * implements a basic <a href="https://en.wikipedia.org/wiki/Cache_replacement_policies#LRU">LRU replacement policy</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.1, May 20, 2019
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/">Plugging memory leaks with soft references</a>
 * @see <a href="https://dzone.com/articles/weak-soft-and-phantom-references-in-java-and-why-they-matter">Weak, Soft,
 *     and Phantom References in Java (and Why They Matter)</a>
 */
public class SoftCache<K, V> {

    protected static final int DEFAULT_MAX_SIZE = 1024;

    protected final int maxSize;

    protected transient SoftReference<Map<K, V>> cacheRef;

    /** Equivalent to calling <code>new SoftCache({@link #DEFAULT_MAX_SIZE})</code>. */
    public SoftCache() { this(DEFAULT_MAX_SIZE); }

    public SoftCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum cache size must be greater than 0");
        }
        this.maxSize = maxSize;
        this.cacheRef = new SoftReference<>(createBoundedCache(this.maxSize));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> computation) {
        Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        if (cache == null) {
            cacheRef = new SoftReference<>(cache = createBoundedCache(maxSize));
        }
        return cache.computeIfAbsent(key, computation);
    }

    public V get(K key) {
        final Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        return cache == null ? null : cache.get(key);
    }

    public boolean contains(K key) {
        final Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        return cache != null && cache.containsKey(key);
    }

    public V remove(K key) {
        final Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        return cache == null ? null : cache.remove(key);
    }

    public void clear() { cacheRef = null; }

    public int size() {
        final Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        return cache == null ? 0 : cache.size();
    }

    public int maxSize() { return maxSize; }

    public boolean isFull() { return size() == maxSize(); }

    /**
     * Override in order to change the implementation details of the actual storage structure. The default
     * implementation returns a {@link LinkedHashMap} instance configured to represent a <em>LRU</em> cache.
     *
     * @param maxSize the maximum number of elements the cache is able to store
     * @return a new instance of the backing {@link Map structure} to hold data
     */
    protected Map<K, V> createBoundedCache(int maxSize) {
        return new LinkedHashMap<K, V>(16 /* => not too many slots, initially */, 0.75f, true /* => LRU */) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > maxSize; /* => LRU */}
        };
    }
}
