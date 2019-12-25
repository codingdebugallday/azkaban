package org.abigballofmud.azkaban.plugin.sql.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.plugin.sql.exception.SqlJobProcessException;
import org.abigballofmud.azkaban.plugin.sql.model.DatabasePojo;
import org.abigballofmud.azkaban.plugin.sql.util.SqlJobUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.abigballofmud.azkaban.plugin.sql.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.sql.constants.JobPropsKey;
import org.abigballofmud.azkaban.plugin.sql.constants.SqlJobPropKeys;
import org.abigballofmud.azkaban.plugin.sql.service.ExecuteJobService;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/24 16:10
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Logger log;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws SqlJobProcessException {
        this.log = logger;
        // 获取sql脚本配置
        List<String> sqlFilePaths = getSqlFilesFormProps(jobProps);
        // 循环执行配置SQL脚本
        String workDir = jobProps.getString(JobPropsKey.WORKING_DIR.getKey());
        for (String sqlFilePath : sqlFilePaths) {
            String realFilePath;
            if (!SqlJobUtil.isAbsolutePath(sqlFilePath)) {
                realFilePath = String.format("%s%s%s", workDir, CommonConstants.PATH_SPLIT_SYMBOL, sqlFilePath);
            } else {
                realFilePath = sqlFilePath;
            }
            executeSingleSqlFile(jobProps, realFilePath);
        }
    }

    private void executeSingleSqlFile(Props jobProps, String sqlFilePath) throws SqlJobProcessException {
        // 加载配置的sql脚本文件
        File sqlFile = SqlJobUtil.loadSqlFile(sqlFilePath);
        try {
            // 替换SQL脚本参数
            String sqlStr = FileUtils.readFileToString(sqlFile, StandardCharsets.UTF_8.name());
            Map<String, String> params = jobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
            String realSql = SqlJobUtil.replacePlaceHolderForSql(sqlStr, params);
            // 待执行的SQL脚本写入临时文件
            File execSqlFile = SqlJobUtil.generateTempSqlFileForExecute(realSql, jobProps.get(JobPropsKey.WORKING_DIR.getKey()), sqlFile.getName());
            //执行SQL脚本
            DatabasePojo databasePojo = new DatabasePojo(jobProps);
            log.info("[sql job]Database URL:" + databasePojo.getUrl());
            log.info("[sql job]Database USER:" + databasePojo.getUsername());
            log.info("[sql job]  ");
            log.info("[sql job]================= execute sql scripts ===================");
            log.info("[sql job]\r\n" + execSqlFile);
            log.info("[sql job]================= execute sql scripts ===================");
            log.info("[sql job]  ");
            String logPath = genExecuteSqlLogPath(jobProps.get(JobPropsKey.JOB_ATTACHMENT_FILE.getKey()));
            SqlJobUtil.executeSql(execSqlFile, databasePojo, logPath);
        } catch (IOException e) {
            throw new SqlJobProcessException("Sql file to string error", e);
        }

    }

    /**
     * 生成log路径
     *
     * @param attachFile attachFile
     * @return java.lang.String
     * @author isacc 2019/12/24 17:16
     */
    private String genExecuteSqlLogPath(String attachFile) {
        return String.format("%s%s",
                attachFile.substring(0, attachFile.lastIndexOf(CommonConstants.ATTACH_FILE_SUFFIX)),
                CommonConstants.LOG_FILE_SUFFIX);
    }

    public List<String> getSqlFilesFormProps(Props jobProps) throws SqlJobProcessException {
        String scriptsString = jobProps.getString(SqlJobPropKeys.SQL_JOB_SCRIPTS.getKey());
        // 判空
        if (StringUtils.isBlank(scriptsString)) {
            log.error("The sql job has no sql script file to be executed!");
            throw new SqlJobProcessException("The sql job has no sql script file to be executed!");
        }
        // 脚本文件拆分
        String[] scriptArr = scriptsString.split(CommonConstants.SCRIPT_SPLIT_SYMBOL);
        return new LinkedList<>(Arrays.asList(scriptArr));
    }
}
