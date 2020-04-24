package org.abigballpfmud.azkaban.plugin.rest.auth;

import azkaban.utils.Props;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 认证提供
 * </p>
 *
 * @author isacc 2020/4/24 13:37
 * @since 1.0
 */
@FunctionalInterface
public interface AuthProvider {

    /**
     * 为RestTemplate提供认证
     *
     * @param restTemplate RestTemplate
     * @param props        Props
     * @param log          azkaban logger
     */
    void provide(RestTemplate restTemplate, Props props, Logger log);

}
