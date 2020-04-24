package org.abigballpfmud.azkaban.plugin.rest.constants;

import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.abigballpfmud.azkaban.plugin.rest.auth.AuthProvider;
import org.abigballpfmud.azkaban.plugin.rest.auth.BasicAuthProvider;
import org.abigballpfmud.azkaban.plugin.rest.auth.NoneAuthProvider;
import org.abigballpfmud.azkaban.plugin.rest.auth.OAuth2AuthProvider;

/**
 * <p>
 * 认证类型
 * </p>
 *
 * @author isacc 2020/4/24 13:37
 * @since 1.0
 */
public enum Auth {

    /**
     * 不需要认证
     */
    NONE,

    /**
     * 标准oauth2
     */
    OAUTH2,

    /**
     * Basic认证
     */
    BASIC,

    ;

    public AuthProvider authProvider() {
        switch (this) {
            case NONE:
                return new NoneAuthProvider();
            case OAUTH2:
                return new OAuth2AuthProvider();
            case BASIC:
                return new BasicAuthProvider();
            default:
                // never
                throw new CustomerRuntimeException("Need AuthProvider");
        }
    }

}
