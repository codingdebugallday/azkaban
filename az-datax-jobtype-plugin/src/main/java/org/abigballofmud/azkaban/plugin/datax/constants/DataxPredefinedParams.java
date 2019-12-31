package org.abigballofmud.azkaban.plugin.datax.constants;

/**
 * <p>
 * datax插件内置参数
 * </p>
 *
 * @author isacc 2019/12/31 11:17
 * @since 1.0
 */
public interface DataxPredefinedParams {

    /**
     * 当前时间，掩码格式自定，如：${_p_current_data_time:yyyy-MM-dd HH:mm:ss}
     * 默认掩码格式：yyyy-MM-dd HH:mm:ss
     */
    String CURRENT_DATE_TIME = "_p_current_data_time";
}
