package org.abigballofmud.azkaban.common.params;

import java.util.regex.Pattern;

/**
 * <p>
 * 插件内置参数
 * <p>
 * 当前时间：${_p_current_date_time:yyyy-MM-dd HH:mm:ss}
 * 当天:${_p_current_date_time:yyyy-MM-dd}
 * 当月:${_p_current_date_time:yyyy-MM}
 * 当年:${_p_current_date_time:yyyy}
 * <p>
 * 单位有：year，month，week，day，hour，min，sec, N为整数
 * 前N天或后N天：${_p_current_date_time:N:day}
 * 前N小时或后N小时：${_p_current_date_time:N:hour}
 * 前N分钟或后N分钟：${_p_current_date_time:N:min}
 * 前N秒或后N秒：${_p_current_date_time:N:sec}
 * <p>
 * 上次执行时间：${_p_last_date_time:yyyy-MM-dd HH:mm:ss}
 * <p>
 * 当前最大id：${_p_current_max_id}
 * 上次最大id：${_p_last_max_id}
 *
 * @author abigballofmud 2020/01/10 15:48
 * @since 1.0
 */
public interface PredefinedParams {

    Pattern PREDEFINED_PARAM_REGEX = Pattern.compile("\\$\\{(.*?)}");

    /**
     * 当前时间，掩码格式自定，如：${_p_current_date_time:yyyy-MM-dd HH:mm:ss} 默认掩码格式：yyyy-MM-dd HH:mm:ss
     * 当天:${_p_current_date_time:yyyy-MM-dd}
     * 当月:${_p_current_date_time:yyyy-MM}
     * 当年:${_p_current_date_time:yyyy}
     * 前N天或后N天：${_p_current_date_time:N:day} N为正负整数
     * 前N小时或后N小时：${_p_current_date_time:N:hour}
     * 前N分钟或后N分钟：${_p_current_date_time:N:min}
     * 前N秒或后N秒：${_p_current_date_time:N:sec}
     */
    String CURRENT_DATE_TIME = "_p_current_date_time";

    /**
     * 上次执行时间：${_p_last_date_time:yyyy-MM-dd HH:mm:ss} 默认掩码格式：yyyy-MM-dd HH:mm:ss
     */
    String LAST_DATE_TIME = "_p_last_date_time";

    /**
     * 当前最大id：${_p_current_max_id}
     */
    String CURRENT_MAX_ID = "_p_current_max_id";

    /**
     * 上次最大id：${_p_last_max_id}
     */
    String LAST_MAX_ID = "_p_last_max_id";

    String SPLIT_KEY = ":";

    String FAILED = "failed";

}
