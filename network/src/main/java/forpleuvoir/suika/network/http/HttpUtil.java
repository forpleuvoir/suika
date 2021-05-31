package forpleuvoir.suika.network.http;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * 简单的HttpClient调用
 * <p>此类所有的HttpResponse.body 都是字符串{@link String}类型
 *
 * @author forpleuvoir
 * <p>#project_name suika
 * <p>#package forpleuvoir.suika.network.http
 * <p>#class_name HttpUtil
 * <p>#create_time 2021/5/16 1:04
 */
public class HttpUtil {
    private transient static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static final Duration TIMEOUT = Duration.ofMillis(10000);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();

    /**
     * 发送一个Get请求
     *
     * @param uri     链接地址
     * @param params  参数 k:v
     * @param headers 请求头 k:v
     * @return {@link Result}
     */
    public static Result get(String uri, Map<String, Object> params, Map<String, String> headers) {
        return noBody(uri, Type.GET, params, headers);
    }

    /**
     * 发送一个Post请求
     *
     * @param uri     链接地址
     * @param params  参数 Request.Body
     * @param headers 请求头 k:v
     * @return {@link Result}
     */
    public static Result post(String uri, @NonNull String params, Map<String, String> headers) {
        return whitBody(uri, Type.POST, params, headers);
    }

    /**
     * 发送一个Put请求
     *
     * @param uri     链接地址
     * @param params  参数 Request.Body
     * @param headers 请求头 k:v
     * @return {@link Result}
     */
    public static Result put(String uri, @NonNull String params, Map<String, String> headers) {
        return HttpUtil.whitBody(uri, Type.PUT, params, headers);
    }


    /**
     * 发送一个DELETE请求
     *
     * @param uri     链接地址
     * @param params  参数 k:v
     * @param headers 请求头 k:v
     * @return {@link Result}
     */
    public static Result delete(String uri,  Map<String, Object> params, Map<String, String> headers) {
        return HttpUtil.noBody(uri, Type.DELETE, params, headers);
    }


    private static Result whitBody(String uri, Type type, @NonNull String params, Map<String, String> headers) {
        var requestBuilder = HttpRequest.newBuilder(URI.create(uri));
        switch (type) {
            case POST:
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(params));
                break;
            case PUT:
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(params));
                break;
        }
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::setHeader);
        }
        try {
            return Result.getResult(HTTP_CLIENT.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return Result.getExceptionResult(e);
        }
    }

    private static Result noBody(String uri, Type type, Map<String, Object> params, Map<String, String> headers) {
        if (params != null && !params.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(uri).append("?");
            params.forEach((k, v) -> {
                stringBuilder.append(k).append("=").append(v);
                stringBuilder.append("&");
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            uri = stringBuilder.toString();
        }

        var requestBuilder = HttpRequest.newBuilder(URI.create(uri));
        switch (type) {
            case DELETE:
                requestBuilder.DELETE();
                break;
            case GET:
                requestBuilder.GET();
                break;
        }
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::setHeader);
        }
        try {
            return Result.getResult(HTTP_CLIENT.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return Result.getExceptionResult(e);
        }
    }

    private enum Type {
        POST("POST"),
        GET("GET"),
        PUT("PUT"),
        DELETE("DELETE");


        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
