package org.abigballofmud.azkaban.plugin.sqoop.exception;

/**
 * <p>
 * Job自定义异常类
 * </p>
 *
 * @author abigballofmud 2019/12/18 16:43
 * @since 1.0
 */
public class SqoopJobProcessException extends Exception {
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

    public SqoopJobProcessException(String message, String projectName, String flowId, String execId, Throwable cause) {
        super(message, cause);
        this.projectName = projectName;
        this.flowId = flowId;
        this.execId = execId;
    }

    public SqoopJobProcessException() {
    }

    public SqoopJobProcessException(String message) {
        super(message);
    }

    public SqoopJobProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqoopJobProcessException(Throwable cause) {
        super(cause);
    }

    public SqoopJobProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
