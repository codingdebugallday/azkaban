package org.abigballofmud.azkaban.plugin.datax;

import java.util.Collections;
import java.util.List;

import azkaban.jobExecutor.ProcessJob;
import azkaban.utils.Props;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.abigballofmud.azkaban.plugin.datax.binder.DataxBindModule;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxJobProcessException;
import org.abigballofmud.azkaban.plugin.datax.service.ExecuteJobService;
import org.apache.log4j.Logger;

/**
 * <p>
 * datax调度任务实现主类
 * </p>
 *
 * @author abigballofmud 2019/12/18 16:47
 * @since 1.0
 */
public class DataxJob extends ProcessJob {

    /**
     * job配置参数
     */
    private final Props dataxJobProps;

    private static final Injector INJECTOR;

    static {
        INJECTOR = Guice.createInjector(new DataxBindModule());
    }

    public DataxJob(String jobId, Props sysProps, Props jobProps, Logger logger) {
        super(jobId, sysProps, jobProps, logger);
        this.sysProps = sysProps;
        this.dataxJobProps = jobProps;
    }

    /**
     * 这里返回datax执行的命令
     *
     * @return java.util.List<java.lang.String>
     * @author abigballofmud 2019/12/19 19:56
     */
    @Override
    protected List<String> getCommandList() {
        Logger log = this.getLog();
        try {
            ExecuteJobService executeJobService = INJECTOR.getInstance(ExecuteJobService.class);
            return executeJobService.generateDataxCommand(this.dataxJobProps, log);
        } catch (DataxJobProcessException e) {
            log.error("datax job command generate fail", e);
        }
        return Collections.emptyList();
    }

}
