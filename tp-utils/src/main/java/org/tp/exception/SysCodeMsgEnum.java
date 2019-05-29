package org.tp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统编码枚举类
 * @version
 */
@Getter
@AllArgsConstructor
public enum SysCodeMsgEnum {
    /** 成功 */
    SUCCESS("GP_00", "成功"),
    /** 系统正忙 */
    SYS_BUSY("GP_050999", "系统正忙"),
    /** 系统异常 */
    SYS_ERROR("GP_050998", "系统异常"),
    /** 请求报文签名未通过验证 */
    INVALID_SIGN("GP_050001", "请求报文签名未通过验证"),
    /** 您没有该接口服务的权限 */
    PERMISSION_DENY("GP_050002", "您没有该接口服务的权限"),
    /** 请求报文参数有误， {param}填具体有问题的参数名称 */
    INVALID_PARAM("GP_050003", "请求报文{param}参数有误"),
    /** 时间戳不正确业务请求超时 */
    //TIMESTAMP_ERROR("GP_050004", "时间戳不正确业务请求超时"), //should decide valid or not
    /** 参数绑定异常 */
    PARAMETERS_BIND_ERROR("GP_050006","参数绑定错误"),
    /** 参数校验错误 */
    PARAMETERS_VALID_ERROR("GP_050007","参数校验错误"),
    /** 类型转换错误 */
    CLASS_CAST_ERROR("GP_050008","类型转换错误"),
    /** 类型转换错误 */
    NUMBER_FORMAT_ERROR("GP_050009","数值类型转换错误"),;

    /** 编码CODE */
    private String code;
    /** 编码MSG */
    private String msg;
}
