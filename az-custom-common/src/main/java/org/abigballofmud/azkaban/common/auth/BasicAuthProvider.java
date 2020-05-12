package org.abigballofmud.azkaban.common.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.Key;
import org.abigballofmud.azkaban.common.utils.Conf;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * basic认证
 * </p>
 *
 * @author isacc 2020/4/24 14:14
 * @since 1.0
 */
public class BasicAuthProvider implements AuthProvider {

    private String token;

    @Override
    public void provide(RestTemplate restTemplate, Props props, Logger log) {
        log.warn("Basic auth");
        check(props);
        log.info("Add access_token Interceptor");
        // 拦截器
        restTemplate.getInterceptors().add((request, body, execution) -> {
            String header = "Basic " + token;
            request.getHeaders().add("Authorization", header);
            log.info(String.format("Add http header [Authorization=%s]", header));
            return execution.execute(request, body);
        });
    }

    private void check(Props props) {
        String username = Conf.require(props, Key.USERNAME);
        String password = Conf.require(props, Key.PASSWORD);
        this.token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

}
