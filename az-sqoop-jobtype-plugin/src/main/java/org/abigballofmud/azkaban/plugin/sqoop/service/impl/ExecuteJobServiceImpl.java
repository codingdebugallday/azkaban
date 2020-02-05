package org.abigballofmud.azkaban.plugin.sqoop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.domain.SpecifiedParamsResponse;
import org.abigballofmud.azkaban.common.utils.CommonUtil;
import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.plugin.sqoop.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.sqoop.constants.SqoopJobPropKeys;
import org.abigballofmud.azkaban.plugin.sqoop.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.sqoop.utils.SqoopJobUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/02/03 15:04
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Logger log;

    @Override
    public List<String> generateSqoopCommand(Props sqoopJobProps, Logger logger) {
        this.log = logger;
        log.debug("sqoop job start run...");
        List<String> sqoopCommandFromProps = getSqoopCommandFromProps(sqoopJobProps);
        // 生成具体的sqoop命令
        ArrayList<String> list = new ArrayList<>(sqoopCommandFromProps.size());
        sqoopCommandFromProps.forEach(command -> {
            // 替换Json脚本参数
            Map<String, String> params = sqoopJobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
            String jobName = sqoopJobProps.get(JobPropsKey.JOB_ID.getKey());
            String workDir = sqoopJobProps.get(JobPropsKey.WORKING_DIR.getKey());
            String hdspPropertiesPath = CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
            log.info("jobName: " + jobName);
            SpecifiedParamsResponse specifiedParams = ParamsUtil.getSpecifiedParams(
                    ParamsUtil.getHdspCoreUrl(log, hdspPropertiesPath),
                    Long.valueOf(jobName.split("\\.")[0]),
                    jobName);
            log.info("specifiedParams: " + specifiedParams);
            list.add(SqoopJobUtil.replacePlaceHolderForJson(command, params, specifiedParams));
        });
        return list;
    }

    /**
     * 获取sqoop job的command
     *
     * @param jobProps azkaban job参数
     * @return java.util.List<java.lang.String>
     * @author abigballofmud 2019/12/19 10:14
     */
    private List<String> getSqoopCommandFromProps(Props jobProps) {
        String sqoopCommand = jobProps.getString(SqoopJobPropKeys.SQOOP_JOB_COMMAND.getKey());
        if (StringUtils.isBlank(sqoopCommand)) {
            log.warn("任务未配置需要执行的sqoop命令！");
        }
        return Collections.singletonList(sqoopCommand);
    }
}
