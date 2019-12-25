package org.abigballofmud.azkaban.plugin.sql.model;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/25 15:01
 * @since 1.0
 */
public class DataSourceDTO {

    /*{
        "customize": true,
        "datasourceId": 105,
        "schema": "hdsp_test",
        "sql": "select * from table",
        "tenantId": 18
    }*/

    private Boolean customize;
    private Long datasourceId;
    private String schema;
    private String sql;
    private Long tenantId;

    public Boolean getCustomize() {
        return customize;
    }

    public void setCustomize(Boolean customize) {
        this.customize = customize;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "DataSourceDTO{" +
                "customize=" + customize +
                ", datasourceId=" + datasourceId +
                ", schema='" + schema + '\'' +
                ", sql='" + sql + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
