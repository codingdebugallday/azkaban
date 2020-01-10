package org.abigballofmud.azkaban.plugin.sql.exception;

import org.apache.http.HttpStatus;

/**
 * <p>
 * DataxRuntimeException
 * </p>
 *
 * @author abigballofmud 2019/12/19 10:51
 * @since 1.0
 */
public class SqlRuntimeException extends RuntimeException {

    private final Integer code;

    public SqlRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public SqlRuntimeException(String msg) {
        super(msg);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public SqlRuntimeException(Throwable throwable) {
        super(throwable);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public SqlRuntimeException(Integer code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
