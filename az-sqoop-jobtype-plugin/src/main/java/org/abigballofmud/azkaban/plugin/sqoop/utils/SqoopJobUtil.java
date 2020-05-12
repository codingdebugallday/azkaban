package org.abigballofmud.azkaban.plugin.sqoop.utils;

import java.util.Map;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.plugin.sqoop.constants.CommonConstants;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2019/12/19 10:40
 * @since 1.0
 */
public class SqoopJobUtil {

    private SqoopJobUtil() {
        throw new IllegalStateException("util class!");
    }

    public static String replacePlaceHolderForJson(Props sqoopJobProps,
                                                   Logger log,
                                                   String content) {
        Map<String, String> params = sqoopJobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
        String jobName = sqoopJobProps.get(JobPropsKey.JOB_ID.getKey());
        // 处理job传的参数
        if (!(params == null || params.isEmpty())) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String placeHolder = String.format("\\$\\{%s\\%s\\}", CommonConstants.CUSTOM_PREFIX, key);
                content = content.replaceAll(placeHolder, value);
            }
        }
        // 处理内置参数
        ParamsUtil paramsUtil = new ParamsUtil(sqoopJobProps, log);
        return paramsUtil.handlePredefinedParams(content, jobName);
    }

}
