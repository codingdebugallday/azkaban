package org.abigballofmud.azkaban.common.params;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/02/04 14:49
 * @since 1.0
 */
public enum SimpleTimeUnitEnum {
    /**
     * 常用日期单位
     */
    YEAR("year"),
    MONTH("month"),
    WEEK("week"),
    DAY("day"),
    HOUR("hour"),
    MIN("min"),
    SEC("sec");

    private final String unit;

    SimpleTimeUnitEnum(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
