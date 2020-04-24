package org.abigballpfmud.azkaban.plugin.rest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.abigballpfmud.azkaban.plugin.rest.constants.Key;
import org.springframework.util.StringUtils;

/**
 * <p>
 * url工具类
 * </p>
 *
 * @author isacc 2020/4/24 13:51
 * @since 1.0
 */
public final class UrlUtil {

    private static final Pattern PATTERN = Pattern.compile("^((ht|)tps?)://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$");

    private UrlUtil() {
        throw new IllegalStateException();
    }

    public static boolean isValid(String url) {
        Matcher matcher = PATTERN.matcher(url);
        return matcher.find();
    }

    public static String concatUrl(String url, String params) {
        // 没有参数
        if (StringUtils.isEmpty(params)) {
            return url;
        }
        // 已经有&了，直接加&
        if (url.contains(Key.AND)) {
            return url + Key.AND + params;
        }
        // 没有&，有?，直接加&
        if (url.contains(Key.QUESTION)) {
            return url + Key.AND + params;
        }
        // 没有&，没有?，直接加?
        return url + Key.QUESTION + params;
    }

}
