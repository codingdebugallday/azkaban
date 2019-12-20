package org.abigballofmud.azkaban.plugin.datax.entity;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/19 14:21
 * @since 1.0
 */
public class DataxEntity {

    private String syncType;
    private DataxReader reader;
    private DataxWriter writer;
    private Speed speed;
    private ErrorLimit errorLimit;

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public DataxReader getReader() {
        return reader;
    }

    public void setReader(DataxReader reader) {
        this.reader = reader;
    }

    public DataxWriter getWriter() {
        return writer;
    }

    public void setWriter(DataxWriter writer) {
        this.writer = writer;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public ErrorLimit getErrorLimit() {
        return errorLimit;
    }

    public void setErrorLimit(ErrorLimit errorLimit) {
        this.errorLimit = errorLimit;
    }
}
