package org.abigballofmud.azkaban.plugin.sql.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import org.abigballofmud.azkaban.common.utils.ParamsUtil;
import org.abigballofmud.azkaban.plugin.sql.constants.CommonConstants;
import org.abigballofmud.azkaban.plugin.sql.exception.SqlJobProcessException;
import org.abigballofmud.azkaban.plugin.sql.model.DatabasePojo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2019/12/24 16:33
 * @since 1.0
 */
public class SqlJobUtil {

    private SqlJobUtil() {
        throw new IllegalStateException("util class!");
    }

    private static final Pattern WINDOWS_ABSOLUTE_PATH_PATTERN = Pattern.compile("^[\\s\\S]:[\\s\\S]*$");

    /**
     * 是否是绝对路径
     *
     * @param path 路径
     * @return boolean
     * @author abigballofmud 2019/12/24 16:35
     */
    public static boolean isAbsolutePath(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        // linux绝对路径
        if (path.indexOf(CommonConstants.PATH_SPLIT_SYMBOL) == 0) {
            return true;
        }
        // windows绝对路径
        return WINDOWS_ABSOLUTE_PATH_PATTERN.matcher(path).matches();
    }

    /**
     * 加载sql脚本文件
     *
     * @param sqlFilePath sql文件路径
     * @return java.io.File
     * @author abigballofmud 2019/12/24 16:41
     */
    public static File loadSqlFile(String sqlFilePath) throws SqlJobProcessException {
        if (StringUtils.isBlank(sqlFilePath)) {
            throw new SqlJobProcessException("SQL script file path is blank!");
        }
        File sqlFile = new File(sqlFilePath);
        if (!sqlFile.exists() || sqlFile.isDirectory()) {
            throw new SqlJobProcessException(String.format(
                    "The path of the SQL script file is not set correctly or the file does not exist. The currently set path:%s!", sqlFilePath));
        }
        String fileName = sqlFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (!CommonConstants.SQL_FILE_SUFFIX.equals(suffix)) {
            throw new SqlJobProcessException(String.format("%s is not the right sql script file!", fileName));
        }
        return sqlFile;
    }

    /**
     * 替换变量
     *
     * @param sqlStr  sql字符串
     * @param params  参数
     * @param jobName az job_id
     * @return java.lang.String
     * @author abigballofmud 2019/12/24 16:51
     */
    public static String replacePlaceHolderForSql(ParamsUtil paramsUtil,
                                                  String sqlStr,
                                                  Map<String, String> params,
                                                  String jobName) {
        // 处理job传的参数
        if (!(params == null || params.isEmpty())) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String placeHolder = String.format("\\$\\{%s\\%s\\}", CommonConstants.CUSTOM_PREFIX, key);
                sqlStr = sqlStr.replaceAll(placeHolder, value);
            }
        }
        // 处理内置参数
        return paramsUtil.handlePredefinedParams(sqlStr, jobName);
    }

    /**
     * 生成当前任务执行的临时sql文件
     *
     * @param sql        sql
     * @param workingDir azkaban workingDir
     * @param fileName   sql文件名称
     * @return java.io.File
     * @author abigballofmud 2019/12/24 16:56
     */
    public static File generateTempSqlFileForExecute(String sql, String workingDir, String fileName) throws SqlJobProcessException {
        String tempFileName = genTempSqlFileName(workingDir, fileName);
        try {
            File sqlFile = new File(tempFileName);
            FileUtils.writeStringToFile(sqlFile, sql, StandardCharsets.UTF_8.name());
            return sqlFile;
        } catch (IOException e) {
            throw new SqlJobProcessException("Generate temp sql file for execute error", e);
        }
    }

    private static String genTempSqlFileName(String workingDir, String fileName) {
        return String.format("%s/%s_%d_%s",
                workingDir,
                CommonConstants.TEMP_SQL_FILE_NAME_PREFIX,
                System.currentTimeMillis(),
                fileName);
    }

    /**
     * 执行SQL脚本
     *
     * @param sqlFile      sql脚本文件
     * @param databasePojo 数据库配置
     * @param logfile      日志文件路径
     * @author abigballofmud 2019/12/24 17:18
     */
    public static void executeSql(File sqlFile, DatabasePojo databasePojo, String logfile) {
        SQLExec sqlExec = new SQLExec();
        sqlExec.setDriver(databasePojo.getDriver());
        sqlExec.setUrl(databasePojo.getUrl());
        sqlExec.setUserid(databasePojo.getUsername());
        sqlExec.setPassword(databasePojo.getPassword());
        sqlExec.setSrc(sqlFile);
        // 设置是否输出
        sqlExec.setPrint(true);
        sqlExec.setEncoding(StandardCharsets.UTF_8.name());
        sqlExec.setOutput(new File(logfile));
        sqlExec.setAppend(true);
        // 要指定这个属性，不然会出错
        sqlExec.setProject(new Project());
        sqlExec.setAutocommit(true);
        sqlExec.execute();
    }
}
