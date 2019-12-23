package org.abigballofmud.azkaban.plugin.datax.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.abigballofmud.azkaban.plugin.datax.constants.CommonConstants;
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
public class DataxUtil {

    private DataxUtil() {
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
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String placeHolder = String.format("\\$\\{%s\\%s\\}", CommonConstants.CUSTOM_PREFIX, key);
            jsonStr = jsonStr.replaceAll(placeHolder, value);
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
        return String.format("%s/%s%d%s",
                workingDir,
                CommonConstants.TEMP_JSON_FILE_NAME_PREFIX,
                System.currentTimeMillis(),
                fileName);
    }

}