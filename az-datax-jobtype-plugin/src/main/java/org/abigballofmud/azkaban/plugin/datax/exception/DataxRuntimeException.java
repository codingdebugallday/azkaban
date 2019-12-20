package org.abigballofmud.azkaban.plugin.datax.exception;

import org.apache.http.HttpStatus;

/**
 * <p>
 * DataxRuntimeException
 * </p>
 *
 * @author isacc 2019/12/19 10:51
 * @since 1.0
 */
public class DataxRuntimeException extends RuntimeException {

    private final Integer code;

    public DataxRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public DataxRuntimeException(String msg) {
        super(msg);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public DataxRuntimeException(Throwable throwable) {
        super(throwable);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public DataxRuntimeException(Integer code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
