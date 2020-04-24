package org.abigballpfmud.azkaban.plugin.rest.model;

import java.io.Serializable;

/**
 * <p>
 * 数据
 * </p>
 *
 * @author isacc 2020/4/24 14:18
 * @since 1.0
 */
public class Data<T> implements Serializable {

    /**
     * 数据
     */
    private transient T content;

    public Data(T content) {
        this.content = content;
    }

    public Data() {
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Data{" +
                "content=" + content +
                '}';
    }
}
