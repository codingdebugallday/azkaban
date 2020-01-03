package org.abigballofmud.azkaban.plugin.datax.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.abigballofmud.azkaban.plugin.datax.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.datax.constants.DataxPredefinedParams;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxRuntimeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/19 10:40
 * @since 1.0
 */
public class DataxJobUtil {

    private static final Pattern DATAX_JSON_PREDEFINED_PARAM_REGEX = Pattern.compile("\\$\\{(.*?)}");

    private DataxJobUtil() {
        throw new IllegalStateException("util class!");
    }

    /**
     * 加载json文件
     *
     * @param path datax json文件路径
     * @return java.io.File
     * @author isacc 2019/12/19 10:43
     */
    public static File loadJsonFile(String path) {
        if (StringUtils.isBlank(path)) {
            throw new DataxRuntimeException("Datax Json脚本文件路径未设置！");
        }
        File jsonFile = new File(path);
        if (!jsonFile.exists() || jsonFile.isDirectory()) {
            throw new DataxRuntimeException(String.format("Datax Json脚本文件路径未正确设置或者文件不存在，当前设置路径：%s！", path));
        }
        String fileName = jsonFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (!CommonConstants.JSON_FILE_SUFFIX.equals(suffix)) {
            throw new DataxRuntimeException(String.format("%s不是正确的Json脚本文件！", fileName));
        }
        return jsonFile;
    }

    public static String replacePlaceHolderForJson(String jsonStr, Map<String, String> params) {
        // 处理job传的参数
        if (!(params == null || params.isEmpty())) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String placeHolder = String.format("\\$\\{%s\\%s\\}", CommonConstants.CUSTOM_PREFIX, key);
                jsonStr = jsonStr.replaceAll(placeHolder, value);
            }
        }
        // 处理内置参数
        Matcher matcher = DATAX_JSON_PREDEFINED_PARAM_REGEX.matcher(jsonStr);
        while (matcher.find()) {
            // 是否是内置的时间参数
            if (matcher.group(1).trim().contains(DataxPredefinedParams.CURRENT_DATE_TIME)) {
                String currentDataTime;
                if (!matcher.group(1).contains(":")) {
                    // 传了掩码格式 ${_p_current_data_time}
                    currentDataTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    jsonStr = jsonStr.replaceAll(String.format("\\$\\{%s\\}", DataxPredefinedParams.CURRENT_DATE_TIME),
                            currentDataTime);
                } else {
                    // 传了掩码格式 ${_p_current_data_time:yyyy-MM-dd HH:mm:ss}
                    String[] split = matcher.group(1).split(":");
                    currentDataTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(split[1]));
                    jsonStr = jsonStr.replaceAll(String.format("\\$\\{%s\\:%s\\}", split[0], split[1]),
                            currentDataTime);
                }
            }
            // 其他内置参数
        }
        return jsonStr;
    }

    public static File generateTempJsonFileForExecute(String jsonStr, String workingDir, String fileName) throws IOException {
        String tempFileName = genTempJsonFileName(workingDir, fileName);
        File tempFile = new File(tempFileName);
        FileUtils.writeStringToFile(tempFile, jsonStr, StandardCharsets.UTF_8.name());
        return tempFile;
    }

    private static String genTempJsonFileName(String workingDir, String fileName) {
        return String.format("%s/%s_%d_%s",
                workingDir,
                CommonConstants.TEMP_JSON_FILE_NAME_PREFIX,
                System.currentTimeMillis(),
                fileName);
    }

}
