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
     * datax同步类型
     */
    DATAX_JOB_SYNC_TYPE(String.format("%s.sync.type", CUSTOM_PREFIX)),
    /**
     * datax reader用户名
     */
    DATAX_JOB_READER_USERNAME(String.format("%s.reader.username", CUSTOM_PREFIX)),
    /**
     * datax reader密码
     */
    DATAX_JOB_READER_PASSWORD(String.format("%s.reader.password", CUSTOM_PREFIX)),
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
