package net.treset.mc_version_loader.mods.curseforge;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.ModData;

import java.util.List;

public class CurseforgeFiles extends GenericJsonParsable {
    private List<CurseforgeFile> data;
    private CurseforgePagination pagination;

    public CurseforgeFiles(List<CurseforgeFile> data, CurseforgePagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public static CurseforgeFiles fromJson(String json, ModData parent) {
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
