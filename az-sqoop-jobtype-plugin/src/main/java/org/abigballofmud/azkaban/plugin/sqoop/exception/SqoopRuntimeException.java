package org.abigballofmud.azkaban.plugin.sqoop.exception;

import org.apache.http.HttpStatus;

/**
 * <p>
 * DataxRuntimeException
 * </p>
 *
 * @author abigballofmud 2019/12/19 10:51
 * @since 1.0
 */
public class SqoopRuntimeException extends RuntimeException {

    private final Integer code;

    public SqoopRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public SqoopRuntimeException(String msg) {
        super(msg);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public SqoopRuntimeException(Throwable throwable) {
        super(throwable);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public SqoopRuntimeException(Integer code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
