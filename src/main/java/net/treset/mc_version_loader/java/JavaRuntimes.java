package net.treset.mc_version_loader.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaRuntimes extends GenericJsonParsable {
    List<JavaRuntimeOs> runtimes;

    public JavaRuntimes(List<JavaRuntimeOs> runtimes) {
        this.runtimes = runtimes;
    }

    public static JavaRuntimes fromJson(String json) throws SerializationException {
        JsonObject javaObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        Set<Map.Entry<String, JsonElement>> runtimeMembers = JsonUtils.getMembers(javaObj);
        List<JavaRuntimeOs> runtimes = new ArrayList<>();
        if(runtimeMembers != null) {
            for (Map.Entry<String, JsonElement> e : runtimeMembers) {
                runtimes.add(JavaRuntimeOs.fromJsonObject(JsonUtils.getAsJsonObject(e.getValue()), e.getKey()));
            }
        }
        return new JavaRuntimes(runtimes);
    }
    public List<JavaRuntimeOs> getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(List<JavaRuntimeOs> runtimes) {
        this.runtimes = runtimes;
    }
}
