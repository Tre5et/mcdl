package net.treset.mcdl.mods;

public enum ModProvider {
    MODRINTH,
    CURSEFORGE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
