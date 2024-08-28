package net.treset.mcdl.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpUtil {
    public static HttpResponse<byte[]> get(URI url, Map<String, String> headers) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(url);
        headers.forEach(builder::header);
        HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
        return response;
    }

    public static HttpResponse<byte[]> post(URI url, byte[] body, Map<String, String> headers) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(url);
        builder.POST(HttpRequest.BodyPublishers.ofByteArray(body));
        headers.forEach(builder::header);
        HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
        return response;
    }
}
