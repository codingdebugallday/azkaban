package org.abigballofmud.azkaban.plugin.sql.exception;

/**
 * @author: cent
 * @email: 292462859@qq.com
 * @date: 2019/1/22.
 * @description: Job自定义异常类
 */
public class SqlJobProcessException extends Exception {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 工作流ID
     */
    private String flowId;
    /**
     * 调度ID
     */
    private String execId;

    public SqlJobProcessException(String message, String projectName, String flowId, String execId, Throwable cause) {
        super(message, cause);
        this.projectName = projectName;
        this.flowId = flowId;
        this.execId = execId;
    }

    public SqlJobProcessException() {
    }

    public SqlJobProcessException(String message) {
        super(message);
    }

    public SqlJobProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlJobProcessException(Throwable cause) {
        super(cause);
    }

    public SqlJobProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
