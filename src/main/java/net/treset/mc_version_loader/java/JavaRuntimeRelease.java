package net.treset.mc_version_loader.java;

public class JavaRuntimeRelease {
    private String id;
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
        this.id = id;
        this.availability = availability;
        this.manifest = manifest;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
