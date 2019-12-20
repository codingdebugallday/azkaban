package org.abigballofmud.azkaban.plugin.datax;

import java.util.Collections;
import java.util.List;

import azkaban.jobExecutor.ProcessJob;
import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxJobProcessException;
import org.abigballofmud.azkaban.plugin.datax.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.datax.service.impl.ExecuteJobServiceImpl;
import org.apache.log4j.Logger;

/**
 * <p>
 * datax调度任务实现主类
 * </p>
 *
 * @author isacc 2019/12/18 16:47
 * @since 1.0
 */
public class DataxJob extends ProcessJob {

    /**
     * job配置参数
     */
    private Props dataxJobProps;

    private ExecuteJobService executeJobService;

    public DataxJob(String jobId, Props sysProps, Props jobProps, Logger logger) {
        super(jobId, sysProps, jobProps, logger);
        this.sysProps = sysProps;
        this.dataxJobProps = jobProps;
        this.executeJobService = new ExecuteJobServiceImpl();
    }

    /**
     * 这里返回datax执行的命令
     *
     * @return java.util.List<java.lang.String>
     * @author isacc 2019/12/19 19:56
     */
    @Override
    protected List<String> getCommandList() {
        Logger log = this.getLog();
        try {
            return executeJobService.generateDataxCommand(this.dataxJobProps, log);
        } catch (DataxJobProcessException e) {
            log.error("datax job command generate fail", e);
        }
        return Collections.emptyList();
    }

}
