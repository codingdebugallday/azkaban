package org.abigballofmud.azkaban.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

import org.abigballofmud.azkaban.common.params.PredefinedParams;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/01/10 15:54
 * @since 1.0
 */
public class ParamsUtil {

    private ParamsUtil(){
        throw new IllegalStateException("context class");
    }

    public static String handlePredefinedParams(String str){
        Matcher matcher = PredefinedParams.PREDEFINED_PARAM_REGEX.matcher(str);
        while (matcher.find()) {
            // 是否是内置的时间参数
            if (matcher.group(1).trim().contains(PredefinedParams.CURRENT_DATE_TIME)) {
                String currentDataTime;
                if (!matcher.group(1).contains(":")) {
                    // 传了掩码格式 ${_p_current_data_time}
                    currentDataTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    str = str.replaceAll(String.format("\\$\\{%s\\}", PredefinedParams.CURRENT_DATE_TIME),
                            currentDataTime);
                } else {
                    // 传了掩码格式 ${_p_current_data_time:yyyy-MM-dd HH:mm:ss}
                    String[] split = matcher.group(1).split(":");
                    currentDataTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(split[1]));
                    str = str.replaceAll(String.format("\\$\\{%s\\:%s\\}", split[0], split[1]),
                            currentDataTime);
                }
            }
            // 其他内置参数
        }
        return str;
    }
}
