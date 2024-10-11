package dev.treset.mcdl.quiltmc;

import com.google.gson.annotations.SerializedName;

public class QuiltLauncherMeta {
    private int version;
    private int minJavaVersion;
    private QuiltLibraries libraries;
    @SerializedName("mainClass")
    private QuiltMainClass mainClass;

    public QuiltLauncherMeta(int version, int minJavaVersion, QuiltLibraries libraries, QuiltMainClass mainClass) {
        this.version = version;
        this.minJavaVersion = minJavaVersion;
        this.libraries = libraries;
        this.mainClass = mainClass;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMinJavaVersion() {
        return minJavaVersion;
    }

    public void setMinJavaVersion(int minJavaVersion) {
        this.minJavaVersion = minJavaVersion;
    }

    public QuiltLibraries getLibraries() {
        return libraries;
    }

    public void setLibraries(QuiltLibraries libraries) {
        this.libraries = libraries;
    }

    public QuiltMainClass getMainClass() {
        return mainClass;
    }

    public void setMainClass(QuiltMainClass mainClass) {
        this.mainClass = mainClass;
    }
}
