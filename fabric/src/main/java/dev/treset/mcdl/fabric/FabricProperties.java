package dev.treset.mcdl.fabric;

public class FabricProperties {
    private boolean useVersionCache = true;

    FabricProperties() {}

    public FabricProperties(boolean useVersionCache) {
        this.useVersionCache = useVersionCache;
    }

    public boolean isUseVersionCache() {
        return useVersionCache;
    }

    public FabricProperties useVersionCache() {
        this.useVersionCache = true;
        return this;
    }
}
