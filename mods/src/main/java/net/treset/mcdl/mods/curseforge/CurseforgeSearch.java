package net.treset.mcdl.mods.curseforge;

import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;

import java.util.List;

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
