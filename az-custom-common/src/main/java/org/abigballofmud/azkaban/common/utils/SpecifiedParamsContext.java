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

    private static final ThreadLocal<SpecifiedParamsResponse> CALLER_INFO_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static SpecifiedParamsResponse current() {
        return CALLER_INFO_THREAD_LOCAL.get();
    }

    public static void setSpecifiedParamsResponse(SpecifiedParamsResponse specifiedParamsResponse) {
        CALLER_INFO_THREAD_LOCAL.set(specifiedParamsResponse);
    }

    public static void clear() {
        CALLER_INFO_THREAD_LOCAL.remove();
    }

}
