package org.abigballofmud.azkaban.plugin.sqoop;

import java.util.Collections;
import java.util.List;

import azkaban.jobExecutor.ProcessJob;
import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.sqoop.exception.SqoopJobProcessException;
import org.abigballofmud.azkaban.plugin.sqoop.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.sqoop.service.impl.ExecuteJobServiceImpl;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/02/03 14:56
 * @since 1.0
 */
public class SqoopJob extends ProcessJob {

    /**
     * job配置参数
     */
    private Props sqoopJobProps;

    private ExecuteJobService executeJobService;

    public SqoopJob(String jobId, Props sysProps, Props jobProps, Logger log) {
        super(jobId, sysProps, jobProps, log);
        this.sysProps = sysProps;
        this.sqoopJobProps = jobProps;
        this.executeJobService = new ExecuteJobServiceImpl();
    }

    /**
     * 这里返回sqoop执行的命令
     *
     * @return java.util.List<java.lang.String>
     * @author abigballofmud 2019/02/03 19:56
     */
    @Override
    protected List<String> getCommandList() {
        Logger log = this.getLog();
        try {
            return executeJobService.generateSqoopCommand(this.sqoopJobProps, log);
        } catch (SqoopJobProcessException e) {
            log.error("sqoop job command generate fail", e);
        }
        return Collections.emptyList();
    }

}
