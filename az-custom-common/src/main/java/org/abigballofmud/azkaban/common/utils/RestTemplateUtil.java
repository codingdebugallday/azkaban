package org.abigballofmud.azkaban.common.utils;

import java.util.Objects;

import com.google.gson.Gson;
import org.abigballofmud.azkaban.common.domain.SpecifiedParamsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2019/12/25 14:52
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
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        return httpHeaders;
    }

}
