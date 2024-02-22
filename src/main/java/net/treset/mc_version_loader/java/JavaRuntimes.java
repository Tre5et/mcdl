package net.treset.mc_version_loader.java;

import net.treset.mc_version_loader.json.SerializationException;

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
}
