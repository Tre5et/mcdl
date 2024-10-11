package dev.treset.mcdl.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A caching implementation that caches data in memory for the duration of the program's runtime
 * @param <T> the type of data to cache
 */
public class RuntimeCaching<T> extends Caching<T> {
    @Override
    public T get(String key) {
        return getIfValid(key);
    }

    @Override
    public void put(String key, T value, Long validDuration) {
        addToCache(key, value, validDuration);
    }

    @Override
    void onStartup(Consumer<HashMap<String, CacheValue<T>>> setCache) {
    }

    @Override
    void onShutdown(Map<String, CacheValue<T>> cache) {
    }
}
