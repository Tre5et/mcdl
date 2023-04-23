package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeImage {
    private String description;
    private int id;
    private int modId;
    private String thumbnailUrl;
    private String title;
    private String url;

    public CurseforgeImage(String description, int id, int modId, String thumbnailUrl, String title, String url) {
        this.description = description;
        this.id = id;
        this.modId = modId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.url = url;
    }

    public static CurseforgeImage fromJson(JsonObject imageObj) {
        return new CurseforgeImage(
                JsonUtils.getAsString(imageObj, "description"),
                JsonUtils.getAsInt(imageObj, "id"),
                JsonUtils.getAsInt(imageObj, "modId"),
                JsonUtils.getAsString(imageObj, "thumbnailUrl"),
                JsonUtils.getAsString(imageObj, "title"),
                JsonUtils.getAsString(imageObj, "url")
        );
    }

    public static List<CurseforgeImage> parseCurseforgeImages(JsonArray imageArray) {
        List<CurseforgeImage> images = new ArrayList<>();
        if(imageArray != null) {
            for(JsonElement e : imageArray) {
                images.add(fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
