package org.abigballofmud.azkaban.plugin.datax.constants;


import static org.abigballofmud.azkaban.plugin.datax.constants.CommonConstants.CUSTOM_PREFIX;

/**
 * <p>
 * 自定义配置参数
 * </p>
 *
 * @author isacc 2019/12/19 10:21
 * @since 1.0
 */
public enum DataxJobPropKeys {
    /**
     * datax_home
     */
    DATAX_HOME(String.format("%s.home", CUSTOM_PREFIX)),
    /**
     * json
     */
    DATAX_JOB_SCRIPTS(String.format("%s.scripts", CUSTOM_PREFIX));

    private final String key;

    DataxJobPropKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
