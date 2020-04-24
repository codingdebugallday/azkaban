package org.abigballpfmud.azkaban.plugin.rest.utils;

import java.util.function.Supplier;

import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;

/**
 * <p>
 * 重试
 * </p>
 *
 * @author isacc 2020/4/24 13:51
 * @since 1.0
 */
public class Retry {

    private static final int DEFAULT_NUM = 3;

    private final int num;

    private Retry(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Retry num must be great then zero");
        }
        this.num = num;
    }

    public static Retry of() {
        return of(DEFAULT_NUM);
    }

    public static Retry of(int num) {
        return new Retry(num);
    }

    public <T> T doRetry(Supplier<T> supplier) {
        Throwable t = null;
        for (int i = 1; i <= num; i++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                t = e;
            }
        }
        throw new CustomerRuntimeException("Retry " + num + " times error", t);
    }

}
