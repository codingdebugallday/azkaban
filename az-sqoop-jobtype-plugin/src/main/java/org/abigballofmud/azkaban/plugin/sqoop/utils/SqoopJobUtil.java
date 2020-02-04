package org.abigballofmud.azkaban.plugin.sqoop.utils;

import java.util.Map;

import org.abigballofmud.azkaban.common.domain.SpecifiedParamsResponse;
import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.plugin.sqoop.constants.CommonConstants;

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

    public static String replacePlaceHolderForJson(String content,
                                                   Map<String, String> params,
                                                   SpecifiedParamsResponse specifiedParams) {
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
        return ParamsUtil.handlePredefinedParams(content, specifiedParams);
    }

}
