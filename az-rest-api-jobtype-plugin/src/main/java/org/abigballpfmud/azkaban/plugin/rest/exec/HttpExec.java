package org.abigballpfmud.azkaban.plugin.rest.exec;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.abigballofmud.azkaban.common.constants.Key;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.abigballofmud.azkaban.common.utils.JsonUtil;
import org.abigballofmud.azkaban.common.utils.RetryUtil;
import org.abigballofmud.azkaban.common.utils.UrlUtil;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
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
    public MutablePair<ResponseEntity<String>, ResponseEntity<String>> doExec(Payload payload) throws CustomerJobProcessException {
        String uri = uri(payload);
        HttpMethod method = HttpMethod.resolve(payload.getOrThrow(Key.METHOD));
        log.info(String.format("uri: %s, method: %s", uri, method));
        // 拼接url
        String query = payload.getNullable(Key.QUERY);
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
        if (Objects.requireNonNull(method) == HttpMethod.GET) {
            entity = new HttpEntity<>(null, headers);
        } else {
            String body = payload.getNullable(Key.BODY);
            entity = new HttpEntity<>(body, headers);
        }
        return handle(payload, url, method, entity);
    }

    private MutablePair<ResponseEntity<String>, ResponseEntity<String>> handle(
            Payload payload, String url, HttpMethod method, HttpEntity<String> entity) throws CustomerJobProcessException {
        // 是否启用重试
        boolean retryEnabled = Boolean.parseBoolean(payload.getOrThrow(Key.ENABLED_RETRY));
        boolean callbackEnabled = Boolean.parseBoolean(payload.getOrThrow(Key.ENABLED_CALLBACK));
        ResponseEntity<String> responseEntity = null;
        ResponseEntity<String> callbackResponseEntity = null;
        if (retryEnabled) {
            // api 重试
            try {
                responseEntity = doRetry(payload, url, method, entity);
            } catch (Exception e) {
                log.error("api error,", e);
            }
        } else {
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }
        // 这里做判断主api的执行情况 若非2xx 直接抛异常
        if (responseEntity != null && !responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new CustomerJobProcessException("request failed, response: " + responseEntity);
        }
        if (callbackEnabled) {
            // 异步回调
            try {
                TimeUnit.SECONDS.sleep(Long.parseLong(payload.getOrThrow(Key.TRIGGER_INTERVAL)));
                callbackResponseEntity = doCallback(payload,
                        payload.getOrThrow(Key.CALLBACK_URI),
                        new HttpEntity<>(null, putHeader(payload)));
            } catch (Exception e) {
                log.error("callback error,", e);
            }
        }
        return MutablePair.of(responseEntity, callbackResponseEntity);
    }

    private ResponseEntity<String> doCallback(Payload payload,
                                              String url,
                                              HttpEntity<String> entity) {
        return RetryUtil.executeWithRetry(() -> {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                    String body = responseEntity.getBody();
                    if (responseEntity.getStatusCode().is2xxSuccessful() && body != null) {
                        // 解析body json格式字符串 根据配置的key获取value 与配置的value对比 相等则执行完成
                        String responseValue = JsonUtil.getJsonNodeValue(new ObjectMapper().readTree(body),
                                payload.getOrThrow(Key.CALLBACK_RESPONSE_KEY));
                        String compareValue = payload.getOrThrow(Key.CALLBACK_RESPONSE_VALUE);
                        log.info("responseValue: " + responseValue + ", compareValue: " + compareValue);
                        if (!compareValue.equals(responseValue)) {
                            throw new CustomerRuntimeException("callback one, the api is running or failed, response: " + body);
                        } else {
                            log.info("callback success, response: " + body);
                        }
                    } else {
                        log.info("callback success, but response is not 2xx: " + body);
                    }
                    return responseEntity;
                },
                Integer.parseInt(payload.getOrThrow(Key.CALLBACK_NUMBER)),
                Integer.parseInt(payload.getNullable(Key.CALLBACK_INTERVAL)) * 1000L,
                Boolean.parseBoolean(payload.getOrThrow(Key.ENABLED_CALLBACK_EXPONENTIAL)));
    }

    private ResponseEntity<String> doRetry(Payload payload,
                                           String url,
                                           HttpMethod method,
                                           HttpEntity<String> entity) {
        return RetryUtil.executeWithRetry(() ->
                        restTemplate.exchange(url, method, entity, String.class),
                Integer.parseInt(payload.getOrThrow(Key.RETRY_NUMBER)),
                Integer.parseInt(payload.getNullable(Key.RETRY_INTERVAL)) * 1000L,
                Boolean.parseBoolean(payload.getOrThrow(Key.ENABLED_RETRY_EXPONENTIAL)));
    }

    @SuppressWarnings("unchecked")
    private HttpHeaders putHeader(Payload payload) {
        HttpHeaders headers = new HttpHeaders();
        String contentType = Optional.ofNullable(payload.get(Key.CONTENT_TYPE))
                .orElse(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(new MediaType(contentType.substring(0, contentType.indexOf('/')),
                contentType.substring(contentType.indexOf('/') + 1)));
        String inputHeaderStr = payload.getNullable(Key.HEADER);
        if (!StringUtils.isEmpty(inputHeaderStr)) {
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
