package org.abigballofmud.azkaban.common.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * PropertiesUtil
 * </p>
 *
 * @author isacc 2020/4/29 10:50
 * @since 1.0
 */
public final class PropertiesUtil {

    private PropertiesUtil() {

    }

    public static String getProperties(String propertiesPath, String key) {
        Properties properties = new Properties();
        try (InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath))) {
            properties.load(in);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("获取[%s]的值出错, %s", key, e));
        }
    }

}
