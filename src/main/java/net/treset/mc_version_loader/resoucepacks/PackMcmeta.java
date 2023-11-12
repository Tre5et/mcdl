package net.treset.mc_version_loader.resoucepacks;

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
        private int pack_format;
        private String description;

        public Pack(int pack_format, String description) {
            this.pack_format = pack_format;
            this.description = description;
        }

        public int getPackFormat() {
            return pack_format;
        }

        public void setPackFormat(int pack_format) {
            this.pack_format = pack_format;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
