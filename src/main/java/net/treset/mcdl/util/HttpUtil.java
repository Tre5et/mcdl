package net.treset.mcdl.util;

import net.treset.mcdl.format.FormatUtils;
import net.treset.mcdl.util.cache.Caching;
import net.treset.mcdl.util.cache.RuntimeCaching;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class HttpUtil {
    private static Caching<HttpResponse<byte[]>> defaultCaching = new RuntimeCaching<>();

    /**
     * Gets the result from a GET request to the specified URL as a string using the default caching strategy
     * @param url The URL to send the request to
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getString(String url) throws IOException {
        return getString(url, defaultCaching);
    }

    /**
     * Gets the result from a GET request to the specified URL as a string using the default caching strategy
     * @param url The URL to send the request to
     * @param caching The caching strategy to use
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getString(String url, Caching<HttpResponse<byte[]>> caching) throws IOException {
        return getString(url, Map.of(), Map.of(), caching);
    }

    /**
     * Gets the result from a GET request to the specified URL as a string using the default caching strategy
     * @param url The URL to send the request to
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getString(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return getString(url, headers, params, defaultCaching);
    }

    /**
     * Gets the result from a GET request to the specified URL as a string
     * @param url The URL to send the request to
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param caching The caching strategy to use
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getString(String url, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        return new String(
                get(new URL(url), headers, params, caching).body()
        );
    }

    /**
     * Sends a GET request to the specified URL using the default caching strategy
     * @param url The URL to send the request to
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> get(URL url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return get(url, headers, params, defaultCaching);
    }

    /**
     * Sends a GET request to the specified URL
     * @param url The URL to send the request to
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param caching The caching strategy to use
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> get(URL url, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        URI uri = constructParamUri(url, params);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
        headers.forEach(builder::header);

        String cacheKey = constructCacheKey("GET", uri, headers, new byte[0]);
        HttpResponse<byte[]> cached = caching.get(cacheKey);
        if(cached != null) {
            return cached;
        }

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            caching.put(cacheKey, response, getCacheTime(response.headers()));
            return response;
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    /**
     * Sends a POST request to the specified URL using the default caching strategy
     * @param url The URL to send the request to
     * @param body The body of the request
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> post(URL url, byte[] body, Map<String, String> headers, Map<String, String> params) throws IOException {
        return post(url, body, headers, params, defaultCaching);
    }

    /**
     * Sends a POST request to the specified URL
     * @param url The URL to send the request to
     * @param body The body of the request
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param caching The caching strategy to use
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> post(URL url, byte[] body, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        URI uri = constructParamUri(url, params);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
        builder.POST(HttpRequest.BodyPublishers.ofByteArray(body));
        headers.forEach(builder::header);

        String cacheKey = constructCacheKey("POST", uri, headers, body);
        HttpResponse<byte[]> cached = caching.get(cacheKey);
        if(cached != null) {
            return cached;
        }

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            caching.put(cacheKey, response, getCacheTime(response.headers()));
            return response;
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    /**
     * Sets the default caching strategy for all requests. Default is {@link RuntimeCaching}
     * @param caching The caching strategy to use
     */
    public static void setDefaultCaching(Caching<HttpResponse<byte[]>> caching) {
        defaultCaching = caching;
    }

    private static URI constructParamUri(URL url, Map<String, String> params) throws IOException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);
        if(!params.isEmpty()) {
            urlBuilder.append("?");
            for(Map.Entry<String, String> p : params.entrySet()) {
                urlBuilder.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
                        .append("=").append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
                        .append("&");
            }
        }
        try {
            return new URI(urlBuilder.toString());
        } catch (URISyntaxException e) {
            throw new IOException("Unable to construct URI", e);
        }
    }

    private static String constructCacheKey(String method, URI url, Map<String, String> headers, byte[] body) {
        return method + ":" + url.toString() + ":" + String.join(";", headers.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).toArray(String[]::new)) + ":" + new String(body);
    }

    private static Long getCacheTime(HttpHeaders headers) {
        long currentTime = System.currentTimeMillis();

        Optional<String> cacheControlHeader = headers.firstValue("Cache-Control");
        if(cacheControlHeader.isPresent()) {
            String cacheControl = cacheControlHeader.get();
            if(cacheControl.contains("max-age=")) {
                String maxAge = FormatUtils.firstGroup(cacheControl, "max-age=(\\d+)");
                if(maxAge != null) {
                    return currentTime + Long.parseLong(maxAge) * 1000;
                }
            }
        }
        return null;
    }
}
