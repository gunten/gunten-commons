package org.tp.enums;

import java.util.Objects;
import java.util.stream.Stream;

public enum ContextEnum {
    /**
     * 用户号
     */
    SUBUSER_NO("subuser_no", "用户号"),
    /**
     * 商户号
     */
    ENTERPRISE_CODE("enterprise_code", "商户号"),
    /**
     * 产品号
     */
    PROD_CODE("prod_code", "产品号"),
    /**
     * 风控数据
     */
    RCC_DATA("event_tracking_data", "风控数据"),
    /**
     * 渠道流水号
     */
    CHANNEL_SERIAL_NO("channel_serial_no", "渠道流水号"),
    /**
     * 风控历史数据
     */
    HISTORY_DATA("history_data", "风控历史数据");

    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    ContextEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContextEnum find(String code) {
        return Stream.of(ContextEnum.values())
                     .filter(e -> Objects.equals(e.getCode(), code))
                     .findAny()
                     .orElse(null);
    }
}