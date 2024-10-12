package dev.treset.mcdl.mods.curseforge;

public class CurseforgeModule {
    private long fingerprint;
    private String name;

    public CurseforgeModule(long fingerprint, String name) {
        this.fingerprint = fingerprint;
        this.name = name;
    }

    public long getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(long fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
