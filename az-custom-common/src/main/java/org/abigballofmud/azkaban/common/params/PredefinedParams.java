package org.abigballofmud.azkaban.common.params;

import java.util.regex.Pattern;

/**
 * <p>
 * 插件内置参数
 * </p>
 *
 * @author abigballofmud 2020/01/10 15:48
 * @since 1.0
 */
public interface PredefinedParams {

    Pattern PREDEFINED_PARAM_REGEX = Pattern.compile("\\$\\{(.*?)}");

    /**
     * 当前时间，掩码格式自定，如：${_p_current_data_time:yyyy-MM-dd HH:mm:ss}
     * 默认掩码格式：yyyy-MM-dd HH:mm:ss
     */
    String CURRENT_DATE_TIME = "_p_current_data_time";
}
