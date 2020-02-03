package org.abigballofmud.azkaban.plugin.sqoop.constants;


import static org.abigballofmud.azkaban.plugin.sqoop.constants.CommonConstants.CUSTOM_PREFIX;

/**
 * <p>
 * 自定义配置参数
 * </p>
 *
 * @author abigballofmud 2019/12/19 10:21
 * @since 1.0
 */
public enum SqoopJobPropKeys {

    /**
     * command
     */
    SQOOP_JOB_COMMAND(String.format("%s.command", CUSTOM_PREFIX));

    private final String key;

    SqoopJobPropKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
