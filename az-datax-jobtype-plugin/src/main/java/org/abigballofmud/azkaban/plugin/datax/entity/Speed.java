package org.abigballofmud.azkaban.plugin.datax.entity;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/19 16:30
 * @since 1.0
 */
public class Speed {

    private String record;
    private String channel = "3";
    private String speedByte;

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSpeedByte() {
        return speedByte;
    }

    public void setSpeedByte(String speedByte) {
        this.speedByte = speedByte;
    }
}
