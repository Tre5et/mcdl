package net.treset.mc_version_loader.fabric;

public class FabricVersionDetails {
    private FabricIntermediaryData intermediary;
    private FabricLauncherMeta launcherMeta;
    private FabricLoaderData loader;

    public FabricVersionDetails(FabricIntermediaryData intermediary, FabricLauncherMeta launcherMeta, FabricLoaderData loader) {
        this.intermediary = intermediary;
        this.launcherMeta = launcherMeta;
        this.loader = loader;
    }

    public FabricIntermediaryData getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(FabricIntermediaryData intermediary) {
        this.intermediary = intermediary;
    }

    public FabricLauncherMeta getLauncherMeta() {
        return launcherMeta;
    }

    public void setLauncherMeta(FabricLauncherMeta launcherMeta) {
        this.launcherMeta = launcherMeta;
    }

    public FabricLoaderData getLoader() {
        return loader;
    }

    public void setLoader(FabricLoaderData loader) {
        this.loader = loader;
    }
}
