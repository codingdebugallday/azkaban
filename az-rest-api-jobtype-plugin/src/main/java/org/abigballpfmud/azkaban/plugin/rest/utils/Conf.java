package org.abigballpfmud.azkaban.plugin.rest.utils;

import java.util.Properties;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 配置
 * </p>
 *
 * @author isacc 2020/4/24 13:42
 * @since 1.0
 */
public final class Conf {

    private Conf() {
        throw new IllegalStateException();
    }

    public static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            throw new CustomerRuntimeException("Properties [" + key + "] required");
        }
        return value;
    }

    public static String require(Props props, String key) {
        String value = props.getString(key);
        if (StringUtils.isEmpty(value)) {
            throw new CustomerRuntimeException("Properties [" + key + "] required");
        }
        return value;
    }

}
