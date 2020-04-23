package org.abigballofmud.azkaban.common.utils;

import org.abigballofmud.azkaban.common.domain.SpecifiedParamsResponse;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/02/04 21:07
 * @since 1.0
 */
public class SpecifiedParamsContext {

    private SpecifiedParamsContext() {
        throw new IllegalStateException("context class!");
    }

    private static final ThreadLocal<SpecifiedParamsResponse> SPECIFIED_PARAMS_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static SpecifiedParamsResponse current() {
        return SPECIFIED_PARAMS_THREAD_LOCAL.get();
    }

    public static void setSpecifiedParamsResponse(SpecifiedParamsResponse specifiedParamsResponse) {
        SPECIFIED_PARAMS_THREAD_LOCAL.set(specifiedParamsResponse);
    }

    public static void clear() {
        SPECIFIED_PARAMS_THREAD_LOCAL.remove();
    }

}
