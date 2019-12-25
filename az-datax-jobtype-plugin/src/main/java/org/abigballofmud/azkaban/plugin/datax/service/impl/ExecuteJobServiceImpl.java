package org.abigballofmud.azkaban.plugin.datax.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.datax.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.datax.constants.DataxJobPropKeys;
import org.abigballofmud.azkaban.plugin.datax.constants.JobPropsKey;
import org.abigballofmud.azkaban.plugin.datax.exception.DataxJobProcessException;
import org.abigballofmud.azkaban.plugin.datax.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.datax.util.DataxJobUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/18 16:45
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Logger log;

    private static final Pattern WINDOWS_ABSOLUTE_PATH_PATTERN = Pattern.compile("^[\\s\\S]:[\\s\\S]*$");

    @Override
    public List<String> generateDataxCommand(Props dataxJobProps, Logger logger) throws DataxJobProcessException {
        this.log = logger;
        log.debug("datax job start run...");
        // 获取datax scripts的值
        List<String> jsonFilePaths = getDataxJsonFilesFromProps(dataxJobProps);
        // 生成具体的命令
        ArrayList<String> list = new ArrayList<>(jsonFilePaths.size());
        String workDir = dataxJobProps.getString(JobPropsKey.WORKING_DIR.getKey());
        for (String jsonFilePath : jsonFilePaths) {
            String realFilePath = this.isAbsolutePath(jsonFilePath) ? jsonFilePath :
                    workDir + CommonConstants.PATH_SPLIT_SYMBOL + jsonFilePath;
            list.add(generateSingleDataCommand(dataxJobProps, realFilePath));
        }
        return list;
    }

    private String generateSingleDataCommand(Props dataxJobProps, String realFilePath) throws DataxJobProcessException {
        try {
            // 加载配置的json脚本文件
            File jsonFile = DataxJobUtil.loadJsonFile(realFilePath);
            // 替换Json脚本参数
            Map<String, String> params = dataxJobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
            String jsonStr = DataxJobUtil.replacePlaceHolderForJson(
                    FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8.name()), params);
            // 待执行的Json脚本写入临时文件
            File execJsonFile = DataxJobUtil.generateTempJsonFileForExecute(
                    jsonStr, dataxJobProps.get(JobPropsKey.WORKING_DIR.getKey()), jsonFile.getName());
            return String.format("python %s/bin/datax.py %s",
                    Optional.ofNullable(dataxJobProps.get(DataxJobPropKeys.DATAX_HOME.getKey())).orElse("$DATAX_HOME"),
                    execJsonFile.getAbsolutePath());
        } catch (IOException e) {
            log.error(String.format("datax job command generate fail, %s", realFilePath), e);
            throw new DataxJobProcessException("datax job command generate fail", e);
        }
    }

    /**
     * 是否绝对路径
     *
     * @param jsonFilePath json文件路径
     * @return boolean
     * @author isacc 2019/12/19 10:29
     */
    private boolean isAbsolutePath(String jsonFilePath) {
        if (StringUtils.isBlank(jsonFilePath)) {
            return false;
        }
        // linux绝对路径
        if (jsonFilePath.indexOf(CommonConstants.PATH_SPLIT_SYMBOL) == 0) {
            return true;
        }
        // windows绝对路径
        return WINDOWS_ABSOLUTE_PATH_PATTERN.matcher(jsonFilePath).matches();
    }

    /**
     * 获取配置datax json文件路径
     *
     * @param jobProps azkaban job参数
     * @return java.util.List<java.lang.String>
     * @author isacc 2019/12/19 10:14
     */
    private List<String> getDataxJsonFilesFromProps(Props jobProps) {
        String scriptsString = jobProps.getString(DataxJobPropKeys.DATAX_JOB_SCRIPTS.getKey());
        if (StringUtils.isBlank(scriptsString)) {
            log.warn("任务未配置需要执行的JSON脚本文件！");
        }
        // 脚本文件拆分
        String[] scriptArr = scriptsString.split(CommonConstants.SCRIPT_SPLIT_SYMBOL);
        return new LinkedList<>(Arrays.asList(scriptArr));
    }

}
