package org.abigballofmud.azkaban.plugin.sql.constants;


/**
 * <p>
 * 自定义参数配置
 * </p>
 *
 * @author isacc 2019/12/24 16:24
 * @since 1.0
 */
public enum SqlJobPropKeys {
    /**
     * 数据库类型
     */
    SQL_JOB_DATABASE_TYPE(String.format("%s.database.type", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库地址
     */
    SQL_JOB_DATABASE_HOST(String.format("%s.database.host", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库地址
     */
    SQL_JOB_DATABASE_PORT(String.format("%s.database.port", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库名称
     */
    SQL_JOB_DATABASE_DATABASE(String.format("%s.database.database", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库实例（模式），针对postgresql
     */
    SQL_JOB_DATABASE_SCHEMA(String.format("%s.database.schema", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 用户名
     */
    SQL_JOB_DATABASE_USERNAME(String.format("%s.database.username", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 密码
     */
    SQL_JOB_DATABASE_PASSWORD(String.format("%s.database.password", CommonConstants.CUSTOM_PREFIX)),
    /**
     * SQL脚本
     */
    SQL_JOB_SCRIPTS(String.format("%s.scripts", CommonConstants.CUSTOM_PREFIX));

    private final String key;

    SqlJobPropKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
