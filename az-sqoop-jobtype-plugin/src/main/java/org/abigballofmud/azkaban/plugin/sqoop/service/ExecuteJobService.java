package org.abigballofmud.azkaban.plugin.sqoop.service;

import java.util.List;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.sqoop.exception.SqoopJobProcessException;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/02/03 14:59
 * @since 1.0
 */
public interface ExecuteJobService {

    /**
     * 生成sqoop执行的具体命令
     *
     * @param sqoopJobProps sqoop任务参数
     * @param logger        Logger
     * @return java.util.List<java.lang.String>
     * @throws SqoopJobProcessException SqoopJobProcessException
     * @author abigballofmud 2020/02/03 15:30
     */
    List<String> generateSqoopCommand(Props sqoopJobProps, Logger logger) throws SqoopJobProcessException;
}
