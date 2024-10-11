package net.treset.mcdl.mods.curseforge;

import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import net.treset.mcdl.mods.ModData;

import java.util.List;

public class CurseforgeFiles extends GenericJsonParsable {
    private List<CurseforgeFile> data;
    private CurseforgePagination pagination;

    public CurseforgeFiles(List<CurseforgeFile> data, CurseforgePagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public static CurseforgeFiles fromJson(String json, ModData parent) throws SerializationException {
        CurseforgeFiles out = fromJson(json, CurseforgeFiles.class, JsonUtils.getGsonCamelCase());
        for(CurseforgeFile f : out.data) {
            f.setParentMod(parent);
        }
        return out;
    }

    public List<CurseforgeFile> getData() {
        return data;
    }

    public void setData(List<CurseforgeFile> data) {
        this.data = data;
    }

    public CurseforgePagination getPagination() {
        return pagination;
    }

    public void setPagination(CurseforgePagination pagination) {
        this.pagination = pagination;
    }
}
