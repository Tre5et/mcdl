package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeAuthor {
    private int id;
    private String name;
    private String url;

    public CurseforgeAuthor(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public static CurseforgeAuthor fromJson(JsonObject authorObj) {
        return new CurseforgeAuthor(
                JsonUtils.getAsInt(authorObj, "id"),
                JsonUtils.getAsString(authorObj, "name"),
                JsonUtils.getAsString(authorObj, "url")
        );
    }

    public static List<CurseforgeAuthor> parseCurseforgeAuthors(JsonArray authorsArray) {
        List<CurseforgeAuthor> authors = new ArrayList<>();
        if(authorsArray != null) {
            for(JsonElement a : authorsArray) {
                authors.add(fromJson(JsonUtils.getAsJsonObject(a)));
            }
        }
        return authors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
