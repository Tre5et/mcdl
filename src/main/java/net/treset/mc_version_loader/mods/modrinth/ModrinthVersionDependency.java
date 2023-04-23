package net.treset.mc_version_loader.mods.modrinth;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

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

    public static ModrinthVersionDependency fromJson(JsonObject dependencyObj) {
        return new ModrinthVersionDependency(
                JsonUtils.getAsString(dependencyObj, "dependency_type"),
                JsonUtils.getAsString(dependencyObj, "file_name"),
                JsonUtils.getAsString(dependencyObj, "project_id"),
                JsonUtils.getAsString(dependencyObj, "version_id")
        );
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
