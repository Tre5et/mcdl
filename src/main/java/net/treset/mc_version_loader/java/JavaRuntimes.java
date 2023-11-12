package net.treset.mc_version_loader.java;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.List;

public class JavaRuntimes extends GenericJsonParsable {
    List<JavaRuntimeOs> runtimes;

    public JavaRuntimes(List<JavaRuntimeOs> runtimes) {
        this.runtimes = runtimes;
    }

    public static JavaRuntimes fromJson(String json) throws SerializationException {
        return fromJson(json, JavaRuntimes.class);
    }

    public List<JavaRuntimeOs> getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(List<JavaRuntimeOs> runtimes) {
        this.runtimes = runtimes;
    }
}
