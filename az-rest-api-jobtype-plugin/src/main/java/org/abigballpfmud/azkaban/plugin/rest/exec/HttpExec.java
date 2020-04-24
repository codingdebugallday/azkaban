package org.abigballpfmud.azkaban.plugin.rest.exec;

import java.util.Collections;
import java.util.Map;


import org.abigballpfmud.azkaban.plugin.rest.constants.Key;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.abigballpfmud.azkaban.plugin.rest.utils.UrlUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * HTTP执行
 * </p>
 *
 * @author isacc 2020/4/24 14:08
 * @since 1.0
 */
public class HttpExec implements Exec {

    private final RestTemplate restTemplate;
    private final Logger log;

    public HttpExec(RestTemplate restTemplate, Logger log) {
        this.restTemplate = restTemplate;
        this.log = log;
    }

    @Override
    @SuppressWarnings("all")
    public ResponseEntity<String> doExec(Payload payload) {
        String uri = uri(payload);
        HttpMethod method = HttpMethod.resolve(payload.getOrThrow(Key.METHOD));
        log.info(String.format("uri: %s, method: %s", uri, method));

        // 拼接url
        String query = payload.getOrThrow(Key.QUERY);
        String url = UrlUtil.concatUrl(uri, query);
        log.info(String.format("query: %s, url: %s", query, url));

        // 加入默认的请求头
        HttpHeaders headers = putHeader(payload);
        log.info(String.format("header: %s", headers));

        // 执行
        HttpEntity<String> entity;
        switch (method) {
            case GET:
                entity = new HttpEntity<>(null, headers);
                return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            default:
                String body = payload.getOrThrow(Key.BODY);
                entity = new HttpEntity<>(body, headers);
                return restTemplate.exchange(url, method, entity, String.class);
        }
    }

    private HttpHeaders putHeader(Payload payload) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> header = payload.getOrThrow(Key.HEADER);
        for (Map.Entry<String, Object> entry : header.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            headers.put(key, Collections.singletonList(String.valueOf(value)));
        }
        return headers;
    }

    private String uri(Payload payload) {
        String uri = payload.getOrThrow(Key.EXPRESSION);
        if (UrlUtil.isValid(uri)) {
            return uri;
        }
        throw new IllegalArgumentException("Expression [" + uri + "] invalid");
    }

}
