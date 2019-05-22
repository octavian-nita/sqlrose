package net.appfold.sqlrose.cache;

import java.util.Objects;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.2, May 22, 2019
 */
public final class MemoizedBiFn<T, U, R> implements BiFunction<T, U, R> {

    private final BiFunction<T, U, R> delegate;

    private final SoftCache<Key<T, U>, R> cache;

    public MemoizedBiFn(BiFunction<T, U, R> delegate) {
        this(delegate, SoftCache.DEFAULT_MAX_SIZE);
    }

    public MemoizedBiFn(BiFunction<T, U, R> delegate, int maxMemoized) {
        this(delegate, new ConcurrentSoftCache<>(maxMemoized));
    }

    public MemoizedBiFn(BiFunction<T, U, R> delegate, SoftCache<Key<T, U>, R> cache) {
        this.delegate = requireNonNull(delegate, "Cannot memoize a null bi-function");
        this.cache = requireNonNull(cache, "A bi-memoized function requires a cache");
    }

    public void clearCache() { cache.clear(); }

    public void setMaxMemoized(int maxMemoized) { cache.setMaxSize(maxMemoized); }

    @Override
    public R apply(T t, U u) { return cache.getOrCompute(Key.of(t, u), tAndU -> delegate.apply(t, u)); }

    public static <T, U, R> MemoizedBiFn<T, U, R> memoize(BiFunction<T, U, R> fn) { return new MemoizedBiFn<>(fn); }

    public static <T, U, R> MemoizedBiFn<T, U, R> memoize(BiFunction<T, U, R> fn, int maxMemoized) {
        return new MemoizedBiFn<>(fn, maxMemoized);
    }

    public static <T, U, R> MemoizedBiFn<T, U, R> memoize(BiFunction<T, U, R> fn, SoftCache<Key<T, U>, R> cache) {
        return new MemoizedBiFn<>(fn, cache);
    }

    private static final class Key<F, S> {

        private final F first;

        private final S second;

        private final int hash; // a Key instance is immutable hence we can cache the hash

        private Key(F first, S second) {
            this.first = first;
            this.second = second;
            this.hash = Objects.hash(first, second);
        }

        private static <F, S> Key<F, S> of(F first, S second) { return new Key<>(first, second); }

        @Override
        public String toString() { return "(" + first + ", " + second + ")"; }

        @Override
        public boolean equals(Object o) {
            return this == o ||
                   o != null && getClass() == o.getClass() && Objects.equals(first, ((Key<?, ?>) o).first) &&
                   Objects.equals(second, ((Key<?, ?>) o).second);
        }

        @Override
        public int hashCode() { return hash; }
    }
}
