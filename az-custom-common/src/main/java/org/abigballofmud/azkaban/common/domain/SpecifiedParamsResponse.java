package org.abigballofmud.azkaban.common.domain;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * <p>
 * 内置参数：通过http接口查询返回映射类
 * </p>
 *
 * @author abigballofmud 2020/02/04 14:00
 * @since 1.0
 */
public class SpecifiedParamsResponse {

    @JsonAlias("_p_current_date_time")
    private String currentDataTime;
    @JsonAlias("_p_last_date_time")
    private String lastDateTime;
    @JsonAlias("_p_current_max_id")
    private String currentMaxId;
    @JsonAlias("_p_last_max_id")
    private String lastMaxId;

    /**
     * 接口错误时返回如下信息
     */
    private Boolean failed;
    private String code;
    private String message;
    private String type;

    public String getCurrentDataTime() {
        return currentDataTime;
    }

    public void setCurrentDataTime(String currentDataTime) {
        this.currentDataTime = currentDataTime;
    }

    public String getLastDateTime() {
        return lastDateTime;
    }

    public void setLastDateTime(String lastDateTime) {
        this.lastDateTime = lastDateTime;
    }

    public String getCurrentMaxId() {
        return currentMaxId;
    }

    public void setCurrentMaxId(String currentMaxId) {
        this.currentMaxId = currentMaxId;
    }

    public String getLastMaxId() {
        return lastMaxId;
    }

    public void setLastMaxId(String lastMaxId) {
        this.lastMaxId = lastMaxId;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SpecifiedParamsResponse{" +
                "currentDataTime='" + currentDataTime + '\'' +
                ", lastDateTime='" + lastDateTime + '\'' +
                ", currentMaxId='" + currentMaxId + '\'' +
                ", lastMaxId='" + lastMaxId + '\'' +
                ", failed=" + failed +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
