package org.abigballpfmud.azkaban.plugin.rest.exec;

import java.util.Collections;
import java.util.Map;

import com.google.gson.Gson;
import org.abigballpfmud.azkaban.plugin.rest.constants.Key;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.abigballpfmud.azkaban.plugin.rest.utils.UrlUtil;
import org.apache.log4j.Logger;
import org.springframework.http.*;
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
    private static final Gson GSON = new Gson();

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
        String query = payload.getNullableOrThrow(Key.QUERY);
        String url;
        if (Key.EMPTY_STRING.equals(query)) {
            url = uri;
        } else {
            url = UrlUtil.concatUrl(uri, query);
        }
        log.info(String.format("query: %s, url: %s", query, url));

        // 加入默认的请求头
        HttpHeaders headers = putHeader(payload);

        // 执行
        HttpEntity<String> entity;
        switch (method) {
            case GET:
                entity = new HttpEntity<>(null, headers);
                return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            default:
                String body = payload.getNullableOrThrow(Key.BODY);
                entity = new HttpEntity<>(body, headers);
                return restTemplate.exchange(url, method, entity, String.class);
        }
    }

    @SuppressWarnings("unchecked")
    private HttpHeaders putHeader(Payload payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("application/json"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String inputHeaderStr = payload.getNullableOrThrow(Key.HEADER);
        if (!Key.EMPTY_STRING.equals(inputHeaderStr)) {
            Map<String, String> map = GSON.fromJson(inputHeaderStr, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headers.put(key, Collections.singletonList(value));
                log.info(String.format("add to http header, %s -> %s", key, value));
            }
        }
        return headers;
    }

    private String uri(Payload payload) {
        String uri = payload.getOrThrow(Key.URI);
        if (UrlUtil.isValid(uri)) {
            return uri;
        }
        throw new IllegalArgumentException("Expression [" + uri + "] invalid");
    }

}
