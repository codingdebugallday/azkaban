package org.abigballofmud.azkaban.plugin.sql.constants;


import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 数据库类型定义
 * </p>
 *
 * @author abigballofmud 2019/12/24 17:07
 * @since 1.0
 */
public enum DatabaseTypes {
    /**
     * Mysql数据库
     */
    MYSQL("com.mysql.jdbc.Driver"),
    /**
     * Postgresql数据库
     */
    POSTGRESQL("org.postgresql.Driver");

    /**
     * 数据库驱动
     */
    private final String driver;

    DatabaseTypes(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }

    /**
     * 拼接JDBC连接串。
     * - mysql：jdbc:mysql://host:port/database
     * - postgresql：jdbc:postgresql://host:port/database?currentSchema=schema
     *
     * @param host     host
     * @param port     port
     * @param database database
     * @param schema   schema
     * @return jdbc url
     */
    public String genJdbcUrl(String host, Integer port, String database, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc");
        sb.append(":");
        sb.append(this.name());
        sb.append("://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/");
        sb.append(database);
        // postgresql 需指定schema
        if (DatabaseTypes.POSTGRESQL.equals(this) && StringUtils.isNotBlank(schema)) {
            sb.append("?currentSchema=");
            sb.append(schema);
        }
        return sb.toString();
    }
}
