package org.abigballpfmud.azkaban.plugin.rest.service.impl;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballpfmud.azkaban.plugin.rest.service.ExecuteJobService;
import org.apache.log4j.Logger;

/**
 * <p>
 * ExecuteJobServiceImpl
 * </p>
 *
 * @author isacc 2020/4/23 16:02
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Logger log;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws CustomerJobProcessException {
        this.log = logger;
        log.debug("start rest api job, " + this.getClass());
    }


}
