package dev.treset.mcdl.mods.modrinth;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.mods.ModsDL;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModrinthSearch extends GenericJsonParsable {
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

    public static ModrinthSearch fromJson(String json) throws SerializationException {
        return fromJson(json, ModrinthSearch.class);
    }

    /**
     * Searches Modrinth for a search query
     * @param query The search query
     * @param versions The versions to filter by
     * @param loaders The loaders to filter by
     * @param limit The maximum number of results to return
     * @param offset The offset to start at
     * @return The search results
     * @throws FileDownloadException If an error occurs while downloading the search results
     */
    public static ModrinthSearch search(String query, List<String> versions, List<String> loaders, int limit, int offset) throws FileDownloadException {
        if(ModsDL.getModrinthUserAgent() == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        Map<String, String> params = new HashMap<>();
        if(query != null) {
            params.put(ModsDL.getModrinthSearchQueryParam(), query);
        }
        if(limit > 0) {
            params.put(ModsDL.getModrinthSearchLimitParam(), String.valueOf(limit));
        }
        if(offset >= 0) {
            params.put(ModsDL.getModrinthSearchOffsetParam(), String.valueOf(offset));
        }
        if(versions != null && !versions.isEmpty() || loaders != null && !loaders.isEmpty()) {
            StringBuilder facets = new StringBuilder().append("[[");
            if(versions != null && !versions.isEmpty()) {
                for(int i = 0; i < versions.size(); i++) {
                    facets.append(ModsDL.getModrinthVersionsFacet(versions.get(i)));
                    if(i < versions.size() - 1) {
                        facets.append(",");
                    }
                }
            }
            facets.append("],[");
            if(loaders != null && !loaders.isEmpty()) {
                for(int i = 0; i < loaders.size(); i++) {
                    facets.append(ModsDL.getModrinthCategoryFacet(loaders.get(i)));
                    if(i < loaders.size() - 1) {
                        facets.append(",");
                    }
                }
            }
            facets.append("]]");
            params.put(ModsDL.getModrinthSearchFacetsParam(), facets.toString());
        }
        try {
            String content = HttpUtil.getString(ModsDL.getModrinthSearchUrl(), ModsDL.getModrinthHeaders(ModsDL.getModrinthUserAgent()), params, ModsDL.getCaching());
            return ModrinthSearch.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth search results", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download modrinth search results", e);
        }
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
}
