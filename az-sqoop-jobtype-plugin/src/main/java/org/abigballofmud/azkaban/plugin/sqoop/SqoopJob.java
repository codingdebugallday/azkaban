package org.abigballofmud.azkaban.plugin.sqoop;

import java.util.Collections;
import java.util.List;

import azkaban.jobExecutor.ProcessJob;
import azkaban.utils.Props;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.abigballofmud.azkaban.plugin.sqoop.binder.SqoopBindModule;
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
    private final Props sqoopJobProps;

    private static final Injector INJECTOR;

    static {
        INJECTOR = Guice.createInjector(new SqoopBindModule());
    }

    public SqoopJob(String jobId, Props sysProps, Props jobProps, Logger log) {
        super(jobId, sysProps, jobProps, log);
        this.sysProps = sysProps;
        this.sqoopJobProps = jobProps;
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
            ExecuteJobService executeJobService = INJECTOR.getInstance(ExecuteJobService.class);
            return executeJobService.generateSqoopCommand(this.sqoopJobProps, log);
        } catch (SqoopJobProcessException e) {
            log.error("sqoop job command generate fail", e);
        }
        return Collections.emptyList();
    }

}
