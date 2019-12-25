package org.abigballofmud.azkaban.plugin.sql;

import azkaban.jobExecutor.AbstractJob;
import azkaban.utils.Props;
import azkaban.utils.PropsUtils;
import org.abigballofmud.azkaban.plugin.sql.exception.SqlJobProcessException;
import org.abigballofmud.azkaban.plugin.sql.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.sql.service.impl.ExecuteJobServiceImpl;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/24 20:35
 * @since 1.0
 */
public class SqlJob extends AbstractJob {
    /**
     * azkaban系统参数
     */
    private Props sysProps;
    /**
     * job配置参数
     */
    private Props jobProps;
    /**
     * 任务调度类
     */
    private ExecuteJobService executeJobService;

    /**
     *注意：构造方法必须是public 否则azkaban.utils.callConstructor 反射出现问题
     */
    public SqlJob(String jobid, Props sysProps, Props jobProps, Logger logger) {
        super(jobid, logger);
        this.sysProps = sysProps;
        this.jobProps = jobProps;
        this.executeJobService = new ExecuteJobServiceImpl();
    }

    @Override
    public void run() throws Exception {
        // 转换任务参数键值对
        try {
            this.resolveProps();
        } catch (Exception e) {
            this.handleError(String.format("Sql Job Bad property definition, %s", e.getMessage()), e);
        }
        // 执行job
        try {
            executeJobService.executeJob(this.jobProps, this.getLog());
        } catch (SqlJobProcessException e) {
            handleError(e.getMessage(), e);
        }
    }

    protected void handleError(String errorMsg, Exception e) throws SqlJobProcessException {
        this.error(errorMsg);
        if (e != null) {
            throw new SqlJobProcessException(errorMsg, e);
        } else {
            throw new SqlJobProcessException(errorMsg);
        }
    }

    protected void resolveProps() {
        this.jobProps = PropsUtils.resolveProps(this.jobProps);
    }

    public Props getSysProps() {
        return sysProps;
    }

    public void setSysProps(Props sysProps) {
        this.sysProps = sysProps;
    }

    public Props getJobProps() {
        return jobProps;
    }

    public void setJobProps(Props jobProps) {
        this.jobProps = jobProps;
    }

    public ExecuteJobService getExecuteJobService() {
        return executeJobService;
    }

    public void setExecuteJobService(ExecuteJobService executeJobService) {
        this.executeJobService = executeJobService;
    }
}
