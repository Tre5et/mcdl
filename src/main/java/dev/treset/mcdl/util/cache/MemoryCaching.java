package dev.treset.mcdl.util.cache;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A caching implementation that caches data in memory for the duration of the program's runtime
 */
public class MemoryCaching extends Caching<HttpResponse<byte[]>> {
    @Override
    public HttpResponse<byte[]> get(String key) {
        return getIfValid(key);
    }

    @Override
    public void put(String key, HttpResponse<byte[]> value, Long validDuration) {
        addToCache(key, value, validDuration);
    }

    @Override
    void onStartup(Consumer<HashMap<String, CacheValue<HttpResponse<byte[]>>>> setCache) {
    }

    @Override
    void onShutdown(Map<String, CacheValue<HttpResponse<byte[]>>> cache) {
    }
}
