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
     * 执行sql的方式，有jdbc和http(默认，接口帮我们去查询数据库)
     */
    SQL_EXECUTE_TYPE(String.format("%s.execute.type", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 执行sql的方式为http时，需要指定接口的url
     */
    SQL_HTTP_URL(String.format("%s.http.url", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 执行sql的方式为http时，post的body参数
     */
    SQL_HTTP_BODY(String.format("%s.http.body", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库类型
     */
    SQL_DATABASE_TYPE(String.format("%s.database.type", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库地址
     */
    SQL_DATABASE_HOST(String.format("%s.database.host", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库地址
     */
    SQL_DATABASE_PORT(String.format("%s.database.port", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库名称
     */
    SQL_DATABASE_DATABASE(String.format("%s.database.database", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 数据库实例（模式），针对postgresql
     */
    SQL_DATABASE_SCHEMA(String.format("%s.database.schema", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 用户名
     */
    SQL_DATABASE_USERNAME(String.format("%s.database.username", CommonConstants.CUSTOM_PREFIX)),
    /**
     * 密码
     */
    SQL_DATABASE_PASSWORD(String.format("%s.database.password", CommonConstants.CUSTOM_PREFIX)),
    /**
     * SQL脚本
     */
    SQL_SCRIPTS(String.format("%s.scripts", CommonConstants.CUSTOM_PREFIX));

    private final String key;

    SqlJobPropKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
