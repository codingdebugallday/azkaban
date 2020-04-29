package org.abigballofmud.azkaban.common.utils;

import java.util.regex.Matcher;

import org.abigballofmud.azkaban.common.constants.CommonConstants;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/02/05 14:10
 * @since 1.0
 */
public class CommonUtil {

    private CommonUtil() {
        throw new IllegalStateException("util class");
    }

    public static String getAzHomeByWorkDir(String workDir) {
        Matcher matcher = CommonConstants.AZKABAN_HOME_REGEX.matcher(workDir);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("获取AZKABAN_HOME出错");
    }

    public static String getHdspPropertiesPath(String workDir) {
        return CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
    }
}
