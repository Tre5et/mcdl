package net.treset.mc_version_loader.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaManifest {
    private List<JavaFile> files;

    public JavaManifest(List<JavaFile> files) {
        this.files = files;
    }

    public static JavaManifest fromJson(String dataJson) {
        JsonObject dataObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(dataJson));
        JsonObject filesObj = JsonUtils.getAsJsonObject(dataObj, "files");
        Set<Map.Entry<String, JsonElement>> membersSet = JsonUtils.getMembers(filesObj);
        List<JavaFile> files = new ArrayList<>();
        if(membersSet != null) {
            for(Map.Entry<String, JsonElement> m : membersSet) {
                JsonObject mObj = JsonUtils.getAsJsonObject(m.getValue());
                files.add(JavaFile.fromJson(m.getKey(), mObj));
            }
        }
        return new JavaManifest(files);
    }

    public List<JavaFile> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFile> files) {
        this.files = files;
    }
}
