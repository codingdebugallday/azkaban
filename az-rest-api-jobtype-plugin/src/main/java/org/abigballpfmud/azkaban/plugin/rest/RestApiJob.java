package org.abigballpfmud.azkaban.plugin.rest;

import azkaban.jobExecutor.AbstractJob;
import azkaban.utils.Props;
import azkaban.utils.PropsUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballpfmud.azkaban.plugin.rest.binder.RestApiBindModule;
import org.abigballpfmud.azkaban.plugin.rest.service.ExecuteJobService;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/4/23 16:15
 * @since 1.0
 */
public class RestApiJob extends AbstractJob {
    /**
     * azkaban系统参数
     */
    private Props sysProps;
    /**
     * job配置参数
     */
    private Props jobProps;

    /**
     * 注意：构造方法必须是public 否则azkaban.utils.callConstructor 反射出现问题
     */
    public RestApiJob(String jobid, Props sysProps, Props jobProps, Logger logger) {
        super(jobid, logger);
        this.sysProps = sysProps;
        this.jobProps = jobProps;
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
            this.jobProps.put(JobPropsKey.JOB_ID.getKey(), this.getId());
            Injector injector = Guice.createInjector(new RestApiBindModule());
            ExecuteJobService executeJobService = injector.getInstance(ExecuteJobService.class);
            executeJobService.executeJob(this.jobProps, this.getLog());
        } catch (CustomerJobProcessException e) {
            handleError(e.getMessage(), e);
        }
    }

    protected void handleError(String errorMsg, Exception e) throws CustomerJobProcessException {
        this.error(errorMsg);
        if (e != null) {
            throw new CustomerJobProcessException(errorMsg, e);
        } else {
            throw new CustomerJobProcessException(errorMsg);
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
}
