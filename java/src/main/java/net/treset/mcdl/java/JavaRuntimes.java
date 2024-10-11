package net.treset.mcdl.java;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaRuntimes extends HashMap<String, Map<String, List<JavaRuntimeRelease>>> {
    public JavaRuntimes(Map<String, Map<String, List<JavaRuntimeRelease>>> content) {
        super();
        this.putAll(content);
    }

    public static JavaRuntimes fromJson(String json) throws SerializationException {
        Map<String, Map<String, List<JavaRuntimeRelease>>> runtimes = JavaRuntimeRelease.fromJson(json);
        return new JavaRuntimes(runtimes);
    }

    /**
     * Gets a map of all available java runtimes
     * @return A map of java runtimes. Structure: {os_identifier: {version_id: [release]}}
     * @throws FileDownloadException If there is an error downloading or parsing the runtimes
     */
    public static JavaRuntimes get() throws FileDownloadException {
        try {
            return JavaRuntimes.fromJson(HttpUtil.getString(JavaDL.getJavaRuntimeUrl()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse runtimes file", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to download runtimes file", e);
        }
    }
}
