package org.abigballpfmud.azkaban.plugin.rest.model;

import java.util.HashMap;

/**
 * <p>
 * 参数
 * </p>
 *
 * @author isacc 2020/4/24 14:04
 * @since 1.0
 */
public class Payload extends HashMap<String, String> {

    public static final Payload EMPTY = new Payload();

    public static Payload of() {
        return new Payload();
    }

    public Payload putArgs(String key, String value) {
        put(key, value);
        return this;
    }

    public String getOrThrow(String key) {
        String value = get(key);
        if (value == null) {
            throw new IllegalArgumentException("Argument [" + key + "] required");
        }
        return value;
    }

    public String getNullable(String key) {
        return get(key);
    }

}
