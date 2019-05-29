package org.tp.annotation.sensitive;

/**
 * 脱敏类型工具
 *
 */
public enum SensitiveType {
    /**
     * 中文名
     */
    CHINESE_NAME,
    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 企业组织代码证（三证合一）
     */
    CERT_CODE,
    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     *  公司开户银行联号
     */
    CNAPS_CODE,
    /**
     * JSON类型
     */
    JSON
}
