package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class FabricLibrary {
    private String name;
    private String url;
    private String localPath;
    private String localFileName;

    public FabricLibrary(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static FabricLibrary fromJson(JsonObject libraryObj) {
        return new FabricLibrary(
                JsonUtils.getAsString(libraryObj, "name"),
                JsonUtils.getAsString(libraryObj, "url")
        );
    }

    public static List<FabricLibrary> parseFabricLibraries(JsonArray libraryArray) {
        List<FabricLibrary> libraries = new ArrayList<>();
        if(libraryArray != null) {
            for(JsonElement e : libraryArray) {
                JsonObject eObj = JsonUtils.getAsJsonObject(e);
                libraries.add(fromJson(eObj));
            }
        }
        return libraries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }
}
