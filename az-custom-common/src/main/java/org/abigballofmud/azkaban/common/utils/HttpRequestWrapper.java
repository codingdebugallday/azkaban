package org.abigballofmud.azkaban.common.utils;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

/**
 * <p>
 * HttpRequest包装
 * </p>
 *
 * @author isacc 2020/4/24 13:50
 * @since 1.0
 */
public class HttpRequestWrapper implements HttpRequest {

    private final HttpRequest request;

    public HttpRequestWrapper(HttpRequest request) {
        this.request = request;
    }

    @Override
    public String getMethodValue() {
        return request.getMethodValue();
    }

    @Override
    public URI getURI() {
        return request.getURI();
    }

    @Override
    public HttpHeaders getHeaders() {
        return request.getHeaders();
    }

}
