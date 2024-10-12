package dev.treset.mcdl.mods.curseforge;

public class CurseforgeHash {
    private int algo;
    private String value;

    public CurseforgeHash(int algo, String value) {
        this.algo = algo;
        this.value = value;
    }

    public int getAlgo() {
        return algo;
    }

    public void setAlgo(int algo) {
        this.algo = algo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
