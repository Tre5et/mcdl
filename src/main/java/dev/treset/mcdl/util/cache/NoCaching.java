package dev.treset.mcdl.util.cache;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A caching implementation that does not cache anything
 * @param <T> the type of data to cache
 */
public class NoCaching extends Caching<HttpResponse<byte[]>> {
    @Override
    public HttpResponse<byte[]> get(String key) {
        return null;
    }

    @Override
    public void put(String key, HttpResponse<byte[]> value, Long validDuration) {
    }

    @Override
    void onStartup(Consumer<HashMap<String, CacheValue<HttpResponse<byte[]>>>> setCache) {
    }

    @Override
    void onShutdown(Map<String, CacheValue<HttpResponse<byte[]>>> cache) {
    }
}
