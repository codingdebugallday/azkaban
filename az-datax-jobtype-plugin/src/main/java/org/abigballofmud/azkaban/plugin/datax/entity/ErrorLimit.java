package org.abigballofmud.azkaban.plugin.datax.entity;



/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/19 16:30
 * @since 1.0
 */
public class ErrorLimit {

    private String record = "0";
    private String percentage = "0.02";

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
