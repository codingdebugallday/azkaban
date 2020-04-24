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
public class Payload extends HashMap<String, Object> {

    public static final Payload EMPTY = new Payload();

    public static Payload of() {
        return new Payload();
    }

    public Payload putArgs(String key, Object value) {
        put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrThrow(String key) {
        Object value = get(key);
        if (value == null) {
            throw new IllegalArgumentException("Argument [" + key + "] required");
        }
        try {
            return (T) value;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid argument [" + key + "] type");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getNullableOrThrow(String key) {
        Object value = get(key);
        try {
            return (T) value;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid argument [" + key + "] type");
        }
    }

}
