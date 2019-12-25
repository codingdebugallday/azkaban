package org.abigballofmud.azkaban.plugin.sql.constants;

/**
 * <p>
 * 通用常量类
 * </p>
 *
 * @author isacc 2019/12/24 16:24
 * @since 1.0
 */
public interface CommonConstants {

    /**
     * sql脚本后缀
     */
    String SQL_FILE_SUFFIX = ".sql";
    /**
     * log文件后缀
     */
    String LOG_FILE_SUFFIX = ".log";
    /**
     * attach文件后缀
     */
    String ATTACH_FILE_SUFFIX = ".attach";
    /**
     * 临时sql文件名称
     */
    String TEMP_SQL_FILE_NAME_PREFIXX = "_temp_exec_sql";
    /**
     * 自定义参数前缀
     */
    String CUSTOM_PREFIX = "sql";

    /**
     * 脚本文件分隔符
     */
    String SCRIPT_SPLIT_SYMBOL = ",";

    /**
     * 文件路径分隔符
     */
    String PATH_SPLIT_SYMBOL = "/";
}
