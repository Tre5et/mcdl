package net.treset.mcdl.mods.curseforge;

public class CurseforgeDependency {
    private int modId;
    private int relationType;

    public CurseforgeDependency(int modId, int relationType) {
        this.modId = modId;
        this.relationType = relationType;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
