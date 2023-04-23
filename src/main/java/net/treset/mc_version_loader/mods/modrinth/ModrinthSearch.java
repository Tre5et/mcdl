package net.treset.mc_version_loader.mods.modrinth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ModrinthSearch {
    private List<ModrinthSearchHit> hits;
    private int limit;
    private int offset;
    private int totalHits;

    public ModrinthSearch(List<ModrinthSearchHit> hits, int limit, int offset, int totalHits) {
        this.hits = hits;
        this.limit = limit;
        this.offset = offset;
        this.totalHits = totalHits;
    }

    public List<ModrinthSearchHit> getHits() {
        return hits;
    }

    public void setHits(List<ModrinthSearchHit> hits) {
        this.hits = hits;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public static ModrinthSearch fromJson(String json) {
        JsonObject searchObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new ModrinthSearch(
                parseModrinthSearchHits(JsonUtils.getAsJsonArray(searchObj, "hits")),
                JsonUtils.getAsInt(searchObj, "limit"),
                JsonUtils.getAsInt(searchObj, "offset"),
                JsonUtils.getAsInt(searchObj, "total_hits")
        );
    }

    private static List<ModrinthSearchHit> parseModrinthSearchHits(JsonArray hitsArray) {
        List<ModrinthSearchHit> out = new ArrayList<>();
        if(hitsArray != null) {
            for(JsonElement e : hitsArray) {
                out.add(ModrinthSearchHit.fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return out;
    }

}
