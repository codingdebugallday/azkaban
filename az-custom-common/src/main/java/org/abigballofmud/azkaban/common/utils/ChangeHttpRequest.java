package org.abigballofmud.azkaban.common.utils;

import java.net.URI;

import org.springframework.http.HttpRequest;

/**
 * 修改URI
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class ChangeHttpRequest extends HttpRequestWrapper {

    private final URI uri;

    public ChangeHttpRequest(HttpRequest request, String params) {
        super(request);
        String rawUri = super.getURI().toString();
        this.uri = URI.create(UrlUtil.concatUrl(rawUri, params));
    }

    @Override
    public URI getURI() {
        return uri;
    }

}
