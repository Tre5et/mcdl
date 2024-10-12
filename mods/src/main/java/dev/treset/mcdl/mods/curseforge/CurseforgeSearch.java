package dev.treset.mcdl.mods.curseforge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.mods.ModsDL;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.*;

public class CurseforgeSearch extends GenericJsonParsable {
    private List<CurseforgeMod> data;
    private CurseforgePagination pagination;

    public CurseforgeSearch(List<CurseforgeMod> data, CurseforgePagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public static CurseforgeSearch fromJson(String json) throws SerializationException {
        return fromJson(json, CurseforgeSearch.class, JsonUtils.getGsonCamelCase());
    }

    /**
     * Searches curseforge for mods
     * @param query The search query
     * @param gameVersions The game versions to filter by
     * @param modLoaders The mod loaders to filter by
     * @param limit The maximum number of results to return
     * @param offset The offset to start at
     * @return The search results
     * @throws FileDownloadException If an error occurs while downloading the search results
     */
    public static CurseforgeSearch search(String query, List<String> gameVersions, Set<Integer> modLoaders, int limit, int offset) throws FileDownloadException {
        if(ModsDL.getCurseforgeApiKey() == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        Map<String, String> params = new HashMap<>(ModsDL.getCurseforgeSearchDefaultParams());
        if(query != null && !query.isBlank()) {
            params.put(ModsDL.getCurseforgeSearchQueryParam(), query);
        }
        if(gameVersions != null && !gameVersions.isEmpty()) {
            params.put(ModsDL.getCurseforgeSearchGameversionsParam(), FormatUtils.formatAsArrayParam(gameVersions));
        }
        if(modLoaders != null && !modLoaders.isEmpty()) {
            params.put(ModsDL.getCurseforgeSearchLoadersParam(), FormatUtils.formatAsArrayParam(modLoaders));
        }
        if(limit > 0) {
            params.put(ModsDL.getCurseforgeSearchLimitParam(), String.valueOf(limit));
        }
        if(offset > 0) {
            params.put(ModsDL.getCurseforgeSearchIndexParam(), String.valueOf(offset));
        }
        try {
            String content = HttpUtil.getString(ModsDL.getCurseforgeSearchUrl(), ModsDL.getCurseforgeHeaders(ModsDL.getCurseforgeApiKey()), params);
            return CurseforgeSearch.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge search results", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download curseforge search results", e);
        }
    }

    public List<CurseforgeMod> getData() {
        return data;
    }

    public void setData(List<CurseforgeMod> data) {
        this.data = data;
    }

    public CurseforgePagination getPagination() {
        return pagination;
    }

    public void setPagination(CurseforgePagination pagination) {
        this.pagination = pagination;
    }
}
