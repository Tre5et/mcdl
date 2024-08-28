package net.treset.mcdl.minecraft;


public class MinecraftLogging {

    private Client client;

    public static class Client {

        private String argument;
        private File file;
        private String type;

        public static class File {
            private String id;
            private String sha1;
            private int size;
            private String url;

            public File(String id, String sha1, int size, String url) {
                this.id = id;
                this.sha1 = sha1;
                this.size = size;
                this.url = url;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSha1() {
                return sha1;
            }

            public void setSha1(String sha1) {
                this.sha1 = sha1;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public Client(String argument, File file, String type) {
            this.argument = argument;
            this.file = file;
            this.type = type;
        }

        public String getArgument() {
            return argument;
        }

        public void setArgument(String argument) {
            this.argument = argument;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public MinecraftLogging(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
