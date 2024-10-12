package dev.treset.mcdl.mods.modrinth;

import java.util.Objects;

public class ModrinthVersionDependency {
    private String dependencyType;
    private String fileName;
    private String projectId;
    private String versionId;

    public ModrinthVersionDependency(String dependencyType, String fileName, String projectId, String versionId) {
        this.dependencyType = dependencyType;
        this.fileName = fileName;
        this.projectId = projectId;
        this.versionId = versionId;
    }

    public boolean isRequired() {
        return Objects.equals(dependencyType, "required");
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}
