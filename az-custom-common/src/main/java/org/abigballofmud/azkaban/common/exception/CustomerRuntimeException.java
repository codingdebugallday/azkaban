package org.abigballofmud.azkaban.common.exception;

import org.apache.http.HttpStatus;

/**
 * <p>
 * CustomerRuntimeException
 * </p>
 *
 * @author isacc 2020/4/23 15:57
 * @since 1.0
 */
public class CustomerRuntimeException extends RuntimeException {

    private final Integer code;

    public CustomerRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomerRuntimeException(String msg) {
        super(msg);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public CustomerRuntimeException(Throwable throwable) {
        super(throwable);
        this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public CustomerRuntimeException(Integer code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
