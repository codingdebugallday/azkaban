package org.abigballofmud.azkaban.plugin.sql.service;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.sql.exception.SqlJobProcessException;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/24 16:08
 * @since 1.0
 */
public interface ExecuteJobService {

    /**
     * 执行SQL调度任务
     *
     * @param jobProps job配置参数
     * @param logger   logger
     * @throws SqlJobProcessException SqlJobProcessException
     * @author isacc 2019/12/24 16:09
     */
    void executeJob(Props jobProps, Logger logger) throws SqlJobProcessException;
}
