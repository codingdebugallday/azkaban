package org.abigballofmud.azkaban.common.auth;

import azkaban.utils.Props;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 无须认证
 * </p>
 *
 * @author isacc 2020/4/24 16:11
 * @since 1.0
 */
public class NoneAuthProvider implements AuthProvider {

    @Override
    public void provide(RestTemplate restTemplate, Props props, Logger log) {
        log.warn("None auth");
    }

}
