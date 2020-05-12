package org.abigballofmud.azkaban.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.Auth;
import org.abigballofmud.azkaban.common.constants.Key;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
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
                    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                    factory.setReadTimeout(5000);
                    factory.setConnectTimeout(15000);
                    restTemplate = new RestTemplate(factory);
                    // 这里string序列化使用utf8编码
                    restTemplate.getMessageConverters()
                            .set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                }
            }
        }
        return restTemplate;
    }

    public static RestTemplate getRestTemplateWithAuth(Props props, Logger logger) {
        RestTemplate restTemplate = getRestTemplate();
        configAuth(props, logger);
        return restTemplate;
    }

    private static void configAuth(Props props, Logger logger) {
        // 验证auth
        Auth auth;
        try {
            auth = Auth.valueOf(props.getString(Key.AUTH, Auth.NONE.name()));
        } catch (Exception e) {
            throw new CustomerRuntimeException("Properties [" + Key.METHOD + "] invalid");
        }
        // 设置认证Provider
        auth.authProvider().provide(restTemplate, props, logger);
    }

    public static HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        return httpHeaders;
    }

}
