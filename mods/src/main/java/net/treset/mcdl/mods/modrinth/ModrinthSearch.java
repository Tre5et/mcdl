package net.treset.mcdl.mods.modrinth;

import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

import java.util.List;

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
