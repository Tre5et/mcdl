package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeSearch {
    private List<CurseforgeMod> data;
    private int paginationIndex;
    private int pageSize;
    private int resultsCount;
    private int totalResults;

    public CurseforgeSearch(List<CurseforgeMod> data, int paginationIndex, int pageSize, int resultsCount, int totalResults) {
        this.data = data;
        this.paginationIndex = paginationIndex;
        this.pageSize = pageSize;
        this.resultsCount = resultsCount;
        this.totalResults = totalResults;
    }

    public static CurseforgeSearch fromJson(String json) {
        JsonObject searchObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        JsonObject paginationObj = JsonUtils.getAsJsonObject(searchObj, "pagination");
        return new CurseforgeSearch(
                parseCurseforgeMods(JsonUtils.getAsJsonArray(searchObj, "data")),
                JsonUtils.getAsInt(paginationObj, "index"),
                JsonUtils.getAsInt(paginationObj, "pageSize"),
                JsonUtils.getAsInt(paginationObj, "resultCount"),
                JsonUtils.getAsInt(paginationObj, "totalCount")
        );
    }

    private static List<CurseforgeMod> parseCurseforgeMods(JsonArray modsArray) {
        List<CurseforgeMod> mods = new ArrayList<>();
        if(modsArray != null) {
            for(JsonElement e : modsArray) {
                mods.add(CurseforgeMod.fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return mods;
    }

    public List<CurseforgeMod> getData() {
        return data;
    }

    public void setData(List<CurseforgeMod> data) {
        this.data = data;
    }

    public int getPaginationIndex() {
        return paginationIndex;
    }

    public void setPaginationIndex(int paginationIndex) {
        this.paginationIndex = paginationIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(int resultsCount) {
        this.resultsCount = resultsCount;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
