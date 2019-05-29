package org.tp.enums;

import java.util.Objects;
import java.util.stream.Stream;

/** 线程上下文实现类
 */

public enum DesensitizationEnum {
    /**
     * 姓名
     */
    NAME("userName", "nameDesensition", "姓名"),
    /**
     * 银行卡
     */
    BANKCARD("bankCard", "bankCardDesensition", "银行卡"),
    /**
     * 身份证
     */
    IDCARD("idCard", "idCardDesensition", "身份证"),
    /**
     * 金额
     */
    AMOUNT("amount", "amountDesensition", "金额");

    private String code;
    private String method;
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMethod(){return method;}

    DesensitizationEnum(String code, String method, String description) {
        this.code = code;
        this.method = method;
        this.description = description;
    }

    public static DesensitizationEnum find(String code) {
        return Stream.of(DesensitizationEnum.values())
                     .filter(e -> Objects.equals(e.getCode(), code))
                     .findAny()
                     .orElse(null);
    }
}