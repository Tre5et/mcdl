package dev.treset.mcdl.util;

import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.util.cache.MemoryCaching;
import dev.treset.mcdl.util.cache.Caching;

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
import java.util.function.Function;

public class HttpUtil {
    private static String userAgent = "mcdl/2.1";
    private static Caching<HttpResponse<byte[]>> defaultCaching = new MemoryCaching();

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
     * Gets the result from a GET request to the specified URL as a string and, if applicable, waits for the rate limit to be lifted using the default caching strategy
     * @param url The URL to send the request to
     * @param waitFunction The function to determine how long to wait until retrying the request after being rate limited
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param caching The caching strategy to use
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getStringRateLimited(String url, Function<HttpResponse<byte[]>, Integer> waitFunction, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        return new String(
                getRateLimited(url, waitFunction, headers, params, caching).body()
        );
    }

    /**
     * Gets the result from a GET request to the specified URL as a string and, if applicable, waits for the rate limit to be lifted using the default caching strategy
     * @param url The URL to send the request to
     * @param waitFunction The function to determine how long to wait until retrying the request after being rate limited
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @return The response from the server as a string
     * @throws IOException If there is an error sending the request
     */
    public static String getStringRateLimited(String url, Function<HttpResponse<byte[]>, Integer> waitFunction, Map<String, String> headers, Map<String, String> params) throws IOException {
        return getStringRateLimited(url, waitFunction, headers, params, defaultCaching);
    }

    /**
     * Sends a GET request to the specified URL and, if applicable, waits for the rate limit to be lifted
     * @param url The URL to send the request to
     * @param waitFunction The function to determine how long to wait until retrying the request after being rate limited
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param caching The caching strategy to use
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> getRateLimited(String url, Function<HttpResponse<byte[]>, Integer> waitFunction, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        HttpResponse<byte[]> out = null;
        for(int count = 0; count < 50; count++) {
            HttpResponse<byte[]> response = get(new URL(url), headers, params, caching);
            if(response.statusCode() != 429) {
                out = response;
                break;
            } else {
                String cacheKey = constructCacheKey("GET", constructParamUri(new URL(url), params), headers, new byte[0]);
                caching.put(cacheKey, null);
                int waitTime = waitFunction.apply(response);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ignored) {}
            }
        }
        if(out == null) {
            throw new IOException("Rate limited too many times");
        }
        return out;
    }

    /**
     * Sends a GET request to the specified URL and, if applicable, waits for the rate limit to be lifted using the default caching strategy
     * @param url The URL to send the request to
     * @param waitFunction The function to determine how long to wait until retrying the request after being rate limited
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> getRateLimited(String url, Function<HttpResponse<byte[]>, Integer> waitFunction, Map<String, String> headers, Map<String, String> params) throws IOException {
        return getRateLimited(url, waitFunction, headers, params, defaultCaching);
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
        return get(url, headers, params, true, caching);
    }

    /**
     * Sends a GET request to the specified URL
     * @param url The URL to send the request to
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param autoRedirect Whether requests returning a redirection should be automatically redirected
     * @param caching The caching strategy to use
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> get(URL url, Map<String, String> headers, Map<String, String> params, boolean autoRedirect, Caching<HttpResponse<byte[]>> caching) throws IOException {
        URI uri = constructParamUri(url, params);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
        if(!headers.containsKey("User-Agent")) {
            builder.header("User-Agent", userAgent);
        }
        headers.forEach(builder::header);

        String cacheKey = constructCacheKey("GET", uri, headers, new byte[0]);
        if(caching != null) {
            HttpResponse<byte[]> cached = caching.get(cacheKey);
            if (cached != null) {
                if(autoRedirect) {
                    return returnOrRedirectGet(cached, headers, params, caching);
                }
                return cached;
            }
        }

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            if(caching != null) {
                caching.put(cacheKey, response, getCacheTime(response.headers()));
            }
            if(autoRedirect) {
                return returnOrRedirectGet(response, headers, params, caching);
            }
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
        return post(url, body, headers, params, true, caching);
    }

    /**
     * Sends a POST request to the specified URL
     * @param url The URL to send the request to
     * @param body The body of the request
     * @param headers The headers to send with the request
     * @param params The parameters to send with the request
     * @param autoRedirect Whether requests returning a redirection should be automatically redirected
     * @param caching The caching strategy to use
     * @return The response from the server
     * @throws IOException If there is an error sending the request
     */
    public static HttpResponse<byte[]> post(URL url, byte[] body, Map<String, String> headers, Map<String, String> params, boolean autoRedirect, Caching<HttpResponse<byte[]>> caching) throws IOException {
        URI uri = constructParamUri(url, params);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
        builder.POST(HttpRequest.BodyPublishers.ofByteArray(body));
        if(!headers.containsKey("User-Agent")) {
            builder.header("User-Agent", userAgent);
        }
        headers.forEach(builder::header);

        String cacheKey = constructCacheKey("POST", uri, headers, body);
        HttpResponse<byte[]> cached = caching.get(cacheKey);
        if(cached != null) {
            if(autoRedirect) {
                return returnOrRedirectPost(cached, body, headers, params, caching);
            }
            return cached;
        }

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            caching.put(cacheKey, response, getCacheTime(response.headers()));
            if(autoRedirect) {
                return returnOrRedirectPost(response, body, headers, params, caching);
            }
            return response;
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    /**
     * Sets the default caching strategy for all requests. Default is {@link MemoryCaching}
     * @param caching The caching strategy to use
     */
    public static void setDefaultCaching(Caching<HttpResponse<byte[]>> caching) {
        defaultCaching = caching;
    }

    /**
     * Gets the default user agent for http requests
     * @return The user agent
     */
    public static String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the default user agent for http requests
     * @param userAgent The user agent to use
     */
    public static void setUserAgent(String userAgent) {
        HttpUtil.userAgent = userAgent;
    }

    private static HttpResponse<byte[]> returnOrRedirectGet(HttpResponse<byte[]> httpResponse, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        if(httpResponse.statusCode() >= 301 && httpResponse.statusCode() <= 308) {
            Optional<String> url = httpResponse.headers().firstValue("location");
            if(url.isEmpty()) {
                return httpResponse;
            }
            return get(new URL(url.get()), headers, params, true, caching);
        }
        return httpResponse;
    }

    private static HttpResponse<byte[]> returnOrRedirectPost(HttpResponse<byte[]> httpResponse, byte[] body, Map<String, String> headers, Map<String, String> params, Caching<HttpResponse<byte[]>> caching) throws IOException {
        if(httpResponse.statusCode() >= 301 && httpResponse.statusCode() <= 308) {
            Optional<String> url = httpResponse.headers().firstValue("location");
            if(url.isEmpty()) {
                return httpResponse;
            }
            return post(new URL(url.get()), body, headers, params, true, caching);
        }
        return httpResponse;
    }

    public static Caching<HttpResponse<byte[]>> getDefaultCaching() {
        return defaultCaching;
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
