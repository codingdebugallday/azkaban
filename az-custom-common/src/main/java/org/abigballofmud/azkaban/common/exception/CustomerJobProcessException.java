package org.abigballofmud.azkaban.common.exception;

/**
 * <p>
 * CustomerJobProcessException
 * </p>
 *
 * @author isacc 2020/4/23 15:53
 * @since 1.0
 */
public class CustomerJobProcessException extends Exception {
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

    public CustomerJobProcessException(String message, String projectName, String flowId, String execId, Throwable cause) {
        super(message, cause);
        this.projectName = projectName;
        this.flowId = flowId;
        this.execId = execId;
    }

    public CustomerJobProcessException() {
    }

    public CustomerJobProcessException(String message) {
        super(message);
    }

    public CustomerJobProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerJobProcessException(Throwable cause) {
        super(cause);
    }

    public CustomerJobProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
