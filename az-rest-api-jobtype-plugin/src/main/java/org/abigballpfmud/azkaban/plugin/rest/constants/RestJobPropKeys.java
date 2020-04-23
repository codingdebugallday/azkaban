package org.abigballpfmud.azkaban.plugin.rest.constants;

/**
 * <p>
 * 自定义参数配置
 * </p>
 *
 * @author isacc 2020/4/23 15:19
 * @since 1.0
 */
public enum RestJobPropKeys {
    /**
     * rest url
     */
    REST_URL(String.format("%s.url", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 默认get请求
     */
    REST_METHOD(String.format("%s.method", CommonConstants.CUSTOM_PREFIX)),
    /**
     * body
     */
    REST_BODY(String.format("%s.body", CommonConstants.CUSTOM_PREFIX));

    private final String key;

    RestJobPropKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
