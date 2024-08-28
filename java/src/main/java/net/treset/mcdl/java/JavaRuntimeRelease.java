package net.treset.mcdl.java;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

import java.util.List;
import java.util.Map;

public class JavaRuntimeRelease extends GenericJsonParsable {
    private Availability availability;
    private Manifest manifest;
    private Version version;

    public static class Availability {
        long group;
        long progress;

        public Availability(long group, long progress) {
            this.group = group;
            this.progress = progress;
        }

        public long getGroup() {
            return group;
        }

        public void setGroup(long group) {
            this.group = group;
        }

        public long getProgress() {
            return progress;
        }

        public void setProgress(long progress) {
            this.progress = progress;
        }
    }

    public static class Manifest {
        String sha1;
        long size;
        String url;

        public Manifest(String sha1, long size, String url) {
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Version {
        String name;
        String released;

        public Version(String name, String released) {
            this.name = name;
            this.released = released;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReleased() {
            return released;
        }

        public void setReleased(String released) {
            this.released = released;
        }
    }

    public JavaRuntimeRelease(String id, Availability availability, Manifest manifest, Version version) {
        this.availability = availability;
        this.manifest = manifest;
        this.version = version;
    }

    public static Map<String, Map<String, List<JavaRuntimeRelease>>> fromJson(String json) throws SerializationException {
        return fromJson(json, new TypeToken<>() {});
    }

    public static JavaRuntimeRelease fromJsonObject(JsonObject json, String id) throws SerializationException {
        if(json == null) return new JavaRuntimeRelease(id, null, null, null);
        return GenericJsonParsable.fromJson(json.toString(), JavaRuntimeRelease.class);
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
