package org.abigballofmud.azkaban.plugin.datax.service;

import java.util.List;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxJobProcessException;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/18 16:43
 * @since 1.0
 */
public interface ExecuteJobService {

    /**
     * 生成datax执行的具体命令
     *
     * @param dataxJobProps datax任务参数
     * @param logger        Logger
     * @return java.util.List<java.lang.String>
     * @throws DataxJobProcessException DataxJobProcessException
     * @author isacc 2019/12/19 20:00
     */
    List<String> generateDataxCommand(Props dataxJobProps, Logger logger) throws DataxJobProcessException;
}
