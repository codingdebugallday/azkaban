package org.abigballofmud.azkaban.plugin.sql.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import azkaban.utils.Props;
import com.google.gson.Gson;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.utils.CommonUtil;
import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.common.utils.RestTemplateUtil;
import org.abigballofmud.azkaban.plugin.sql.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.sql.constants.SqlJobPropKeys;
import org.abigballofmud.azkaban.plugin.sql.exception.SqlJobProcessException;
import org.abigballofmud.azkaban.plugin.sql.model.DataSourceDTO;
import org.abigballofmud.azkaban.plugin.sql.model.DatabasePojo;
import org.abigballofmud.azkaban.plugin.sql.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.sql.utils.SqlJobUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2019/12/24 16:10
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Logger log;
    private final Gson gson = new Gson();
    private volatile boolean success;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws SqlJobProcessException {
        this.log = logger;
        // 获取sql脚本配置
        List<String> sqlFilePaths = getSqlFilesFormProps(jobProps);
        // 循环执行配置SQL脚本
        String workDir = jobProps.getString(JobPropsKey.WORKING_DIR.getKey());
        String hdspPropertiesPath = CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
        String hdspCoreUrl = ParamsUtil.getHdspCoreUrl(log, hdspPropertiesPath);
        for (String sqlFilePath : sqlFilePaths) {
            String realFilePath;
            if (!SqlJobUtil.isAbsolutePath(sqlFilePath)) {
                realFilePath = String.format("%s%s%s", workDir, CommonConstants.PATH_SPLIT_SYMBOL, sqlFilePath);
            } else {
                realFilePath = sqlFilePath;
            }
            executeSingleSqlFile(hdspCoreUrl, jobProps, realFilePath);
        }
    }

    private void executeSingleSqlFile(String hdspCoreUrl, Props jobProps, String sqlFilePath) throws SqlJobProcessException {
        // 加载配置的sql脚本文件
        File sqlFile = SqlJobUtil.loadSqlFile(sqlFilePath);
        String jobName = jobProps.get(JobPropsKey.JOB_ID.getKey());
        log.info("jobName: " + jobName);
        try {
            // 替换SQL脚本参数
            String sqlStr = FileUtils.readFileToString(sqlFile, StandardCharsets.UTF_8.name());
            Map<String, String> params = jobProps.getMapByPrefix(CommonConstants.CUSTOM_PREFIX);
            String workDir = jobProps.get(JobPropsKey.WORKING_DIR.getKey());
            String realSql = SqlJobUtil.replacePlaceHolderForSql(log, sqlStr, params, workDir, jobName);
            // 执行SQL脚本
            String executeType = Optional.ofNullable(jobProps.get(SqlJobPropKeys.SQL_EXECUTE_TYPE.getKey()))
                    .orElse(CommonConstants.HTTP);
            if (CommonConstants.HTTP.equalsIgnoreCase(executeType)) {
                // 默认执行方式为http 接口请求 平台客制化功能 不用在意
                executeHttpTypeSql(jobProps, realSql, workDir);
            } else if (CommonConstants.JDBC.equalsIgnoreCase(executeType)) {
                // 一般走jdbc的方式
                // 待执行的SQL脚本写入临时文件
                File execSqlFile = SqlJobUtil.generateTempSqlFileForExecute(realSql,
                        workDir, sqlFile.getName());
                DatabasePojo databasePojo = new DatabasePojo(jobProps);
                log.info("[sql job]jdbc url: " + databasePojo.getUrl());
                log.info("[sql job]user: " + databasePojo.getUsername());
                log.info("[sql job]sql: " + realSql);
                String logPath = genExecuteSqlLogPath(jobProps.get(JobPropsKey.JOB_ATTACHMENT_FILE.getKey()));
                SqlJobUtil.executeSql(execSqlFile, databasePojo, logPath);
                this.success = true;
            } else {
                throw new SqlJobProcessException("Sql execute type error, execute type must in [http, jdbc]");
            }
        } catch (IOException e) {
            this.success = false;
            throw new SqlJobProcessException("Sql file to string error", e);
        } finally {
            // 更新内置参数
            ParamsUtil.updateSpecifiedParams(log, hdspCoreUrl, 0L, jobName, success);
        }

    }

    /**
     * 平台客制化功能，不用关心，因为平台自定义了很多驱动，这里执行sql需要请求接口
     * 一般只需设置sql.execute.type为jdbc即可
     *
     * @param jobProps azkaban job参数
     * @param sql      执行的sql
     * @param workDir  工作目录
     * @author abigballofmud 2019/12/25 14:35
     */
    private void executeHttpTypeSql(Props jobProps, String sql, String workDir) throws SqlJobProcessException {
        // http://192.168.11.212:8510/v2/18/datasources/exec-sql
        // {
        //   "customize": true,
        //   "datasourceId": 105,
        //   "schema": "hdsp_test",
        //   "sql": "INSERT INTO userinfo_text(id,`username`, `password`, `age`, `sex`, `address`) VALUES ( 7,'abigballofmud11', '123456', 24, 1, 'chengdu')",
        //   "tenantId": 18
        // }
        String urlTmp = jobProps.get(SqlJobPropKeys.SQL_HTTP_URL.getKey());
        if(StringUtils.isEmpty(urlTmp)){
            String hdspPropertiesPath = CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
            urlTmp = ParamsUtil.getHdspCoreUrl(log, hdspPropertiesPath);
        }
        String url = Optional.ofNullable(urlTmp)
                .orElseThrow(() -> new SqlJobProcessException("Sql http url is null"));
        String body = Optional.ofNullable(jobProps.get(SqlJobPropKeys.SQL_HTTP_BODY.getKey()))
                .orElseThrow(() -> new SqlJobProcessException("Sql http body is null"));
        DataSourceDTO dataSourceDTO = gson.fromJson(body, DataSourceDTO.class);
        log.info(dataSourceDTO);
        String[] split = sql.trim().split(";");
        for (String exeSql : split) {
            if (StringUtils.isBlank(exeSql)) {
                continue;
            }
            log.info("[sql job]sql: " + exeSql);
            dataSourceDTO.setSql(exeSql);
            RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
            HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(dataSourceDTO), RestTemplateUtil.httpHeaders());
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(
                    String.format("%s/v2/%s/datasources/exec-sql", url, dataSourceDTO.getTenantId()),
                    requestEntity, String.class);
            log.info("[sql job]result: " + stringResponseEntity.getBody());
        }
    }

    /**
     * 生成log路径
     *
     * @param attachFile attachFile
     * @return java.lang.String
     * @author abigballofmud 2019/12/24 17:16
     */
    private String genExecuteSqlLogPath(String attachFile) {
        return String.format("%s%s",
                attachFile.substring(0, attachFile.lastIndexOf(CommonConstants.ATTACH_FILE_SUFFIX)),
                CommonConstants.LOG_FILE_SUFFIX);
    }

    public List<String> getSqlFilesFormProps(Props jobProps) throws SqlJobProcessException {
        String scriptsString = jobProps.getString(SqlJobPropKeys.SQL_SCRIPTS.getKey());
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
