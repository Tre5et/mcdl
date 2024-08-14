package net.treset.mc_version_loader.mods;

public enum ModProvider {
    MODRINTH,
    CURSEFORGE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
