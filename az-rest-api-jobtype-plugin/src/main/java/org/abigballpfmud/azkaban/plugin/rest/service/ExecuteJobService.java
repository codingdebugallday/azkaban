package org.abigballpfmud.azkaban.plugin.rest.service;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/4/23 15:58
 * @since 1.0
 */
public interface ExecuteJobService {

    /**
     * 执行rest api调度任务
     *
     * @param jobProps job配置参数
     * @param logger   logger
     * @throws CustomerJobProcessException CustomerJobProcessException
     * @author abigballofmud 2020/04/23 16:09
     */
    void executeJob(Props jobProps, Logger logger) throws CustomerJobProcessException;
}
