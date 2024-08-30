package net.treset.mcdl.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A caching implementation that does not cache anything
 * @param <T> the type of data to cache
 */
public class NoCaching<T> extends Caching<T> {
    @Override
    public T get(String key) {
        return null;
    }

    @Override
    public void put(String key, T value, Long validDuration) {
    }

    @Override
    void onStartup(Consumer<HashMap<String, CacheValue<T>>> setCache) {
    }

    @Override
    void onShutdown(Map<String, CacheValue<T>> cache) {
    }
}
