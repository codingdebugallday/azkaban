package org.abigballofmud.azkaban.plugin.datax.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.plugin.datax.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxRuntimeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2019/12/19 10:40
 * @since 1.0
 */
public class DataxJobUtil {

    private DataxJobUtil() {
        throw new IllegalStateException("util class!");
    }

    /**
     * 加载json文件
     *
     * @param path datax json文件路径
     * @return java.io.File
     * @author abigballofmud 2019/12/19 10:43
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

    public static String replacePlaceHolderForJson(Props dataxJobProps,
                                                   Logger log,
                                                   String jsonStr) {
        Map<String, String> params = dataxJobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
        String jobName = dataxJobProps.get(JobPropsKey.JOB_ID.getKey());
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
        ParamsUtil paramsUtil = new ParamsUtil(dataxJobProps, log);
        return paramsUtil.handlePredefinedParams(jsonStr, jobName);
    }

    public static File generateTempJsonFileForExecute(Props dataxJobProps,
                                                      String jsonStr,
                                                      String fileName) throws IOException {
        String workDir = dataxJobProps.get(JobPropsKey.WORKING_DIR.getKey());
        String tempFileName = genTempJsonFileName(workDir, fileName);
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
