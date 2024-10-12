package dev.treset.mcdl.java;

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

    /**
     * Gets a map of all available java runtimes for a specified os
     * @param os The os identifier
     * @return A map of java runtimes for the specified os. Structure: {version_id: [release]}
     */
    public Map<String, List<JavaRuntimeRelease>> getOsReleases(String os) {
        return this.get(os);
    }

    /**
     * Gets a list of all available java runtimes for a specified os and version
     * @param os The os identifier
     * @param version The version identifier
     * @return A list of java runtimes for the specified os and version
     */
    public List<JavaRuntimeRelease> getReleases(String os, String version) {
        return this.get(os).get(version);
    }
}
