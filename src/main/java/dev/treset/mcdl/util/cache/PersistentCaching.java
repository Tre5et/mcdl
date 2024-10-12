package dev.treset.mcdl.util.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A caching implementation that stores cache data between program runs
 */
public class PersistentCaching extends MemoryCaching {
    private File cacheFile;

    public PersistentCaching(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    @Override
    void onStartup(Consumer<HashMap<String, CacheValue<HttpResponse<byte[]>>>> setCache) {
        if(cacheFile.exists()) {
            try {
                String content = FileUtil.readFileAsString(cacheFile);
                HashMap<String, CacheValue<HttpResponse<byte[]>>> newCache = new Gson().fromJson(content, new TypeToken<>() {});
                setCache.accept(newCache);
            } catch (IOException e) {
                System.out.println("Failed to read cache file");
                e.printStackTrace(System.out);
            }
        }
    }

    @Override
    void onShutdown(Map<String, CacheValue<HttpResponse<byte[]>>> cache) {
        String content = new Gson().toJson(cache);
        try {
            FileUtil.writeToFile(content.getBytes(), cacheFile);
        } catch (IOException e) {
            System.out.println("Failed to write cache file");
            e.printStackTrace(System.out);
        }
    }

    public File getCacheFile() {
        return cacheFile;
    }

    public void setCacheFile(File cacheFile) {
        this.cacheFile = cacheFile;
    }
}
