package net.appfold.sqlrose.cache;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.2, May 21, 2019
 */
public final class MemoizedFn<T, R> implements Function<T, R> {

    private final Function<T, R> delegate;

    private final SoftCache<T, R> cache;

    public MemoizedFn(Function<T, R> delegate) { this(delegate, SoftCache.DEFAULT_MAX_SIZE); }

    public MemoizedFn(Function<T, R> delegate, int maxMemoized) {
        this.delegate = requireNonNull(delegate, "Cannot memoize a null function");
        this.cache = new ConcurrentSoftCache<>(maxMemoized);
    }

    public MemoizedFn(Function<T, R> delegate, SoftCache<T, R> cache) {
        this.delegate = requireNonNull(delegate, "Cannot memoize a null function");
        this.cache = requireNonNull(cache, "A memoized function requires a cache");
    }

    public void clearCache() { cache.clear(); }

    @Override
    public R apply(T t) { return cache.getOrCompute(t, delegate); }

    public static <T, R> MemoizedFn<T, R> memoize(Function<T, R> fn) { return new MemoizedFn<>(fn); }

    public static <T, R> MemoizedFn<T, R> memoize(Function<T, R> fn, int maxMemoized) {
        return new MemoizedFn<>(fn, maxMemoized);
    }

    public static <T, R> MemoizedFn<T, R> memoize(Function<T, R> fn, SoftCache<T, R> cache) {
        return new MemoizedFn<>(fn, cache);
    }
}
