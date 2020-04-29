package org.abigballofmud.azkaban.common.constants;

import java.util.regex.Pattern;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/02/05 14:09
 * @since 1.0
 */
public class CommonConstants {

    private CommonConstants() {
    }

    public static final Pattern AZKABAN_HOME_REGEX = Pattern.compile("(.*?)/executions");

}
