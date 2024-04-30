package net.treset.mc_version_loader.resoucepacks;

import com.google.gson.annotations.SerializedName;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

public class PackMcmeta extends GenericJsonParsable {
    private Pack pack;

    public PackMcmeta(Pack pack) {
        this.pack = pack;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public static PackMcmeta fromJson(String json) throws SerializationException {
        return fromJson(json, PackMcmeta.class);
    }

    public static class Pack {
        @SerializedName("pack_format")
        private int packFormat;
        private String description;

        public Pack(int packFormat, String description) {
            this.packFormat = packFormat;
            this.description = description;
        }

        public int getPackFormat() {
            return packFormat;
        }

        public void setPackFormat(int pack_format) {
            this.packFormat = pack_format;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
