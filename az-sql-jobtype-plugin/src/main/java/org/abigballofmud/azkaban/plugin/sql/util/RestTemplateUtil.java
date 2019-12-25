package org.abigballofmud.azkaban.plugin.sql.util;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/25 14:52
 * @since 1.0
 */
public class RestTemplateUtil {

    private static volatile RestTemplate restTemplate;

    private RestTemplateUtil() {
        throw new IllegalStateException("util class");
    }

    public static RestTemplate getRestTemplate() {
        if (Objects.isNull(restTemplate)) {
            synchronized (RestTemplateUtil.class) {
                if (Objects.isNull(restTemplate)) {
                    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
                    factory.setReadTimeout(5000);
                    factory.setConnectTimeout(15000);
                    return new RestTemplate(factory);
                }
            }
        }
        return restTemplate;
    }

    public static HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
