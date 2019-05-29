package org.tp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务编码枚举类
 * <p>
 * 后三位定义规则，百分位按照层划分，十分位按组件划分，个位划分具体的错误
 * </p>
 * <p>
 * 0渠道层，1业务层，2核心层，3公共层，4DAO层，5文件操作层，6外部调用层
 * </p>
 * <p>
 * </p>
 *
 */
@Getter
@AllArgsConstructor
public enum BizCodeMsgEnum {

    /**
     * 成功
     */
    SUCCESS("GPBIZ_00", "成功"),
    //*******************************渠道层*************************************** //


    //*******************************业务层*************************************** //
    /**
     * 还款申请处理异常
     */
    BIZ_REPAY_APPLY_ERROR("GPBIZ_050110", "还款申请处理异常"),
    /**
     * 用户授信申请单和授信单状态匹配异常
     */
    USER_CREDIT_FORM_NOT_FOUND("GPBIZ_050130", "首页查询失败"),
    /**
     * 开户失败
     */
    OPEN_CREDIT_PAYMENT_FAIL("GPBIZ_050112", "开户失败"),
    /**
     * 开户需要参数缺失
     */
    OPEN_CREDIT_PARAMETER_NOT_FOUND("GPBIZ_050113", "开户参数缺失"),


    //*******************************核心层*************************************** //
    /**
     * 核心层-合同组件-传入参数有误
     */
    CONTRACT_INPUT_PARAMETER_ERROR("GPBIZ_050201", "传入参数有误!"),
    /**
     * 核心层-合同组件-模板未配置
     */
    CONTRACT_TEMATE_INVALID("GPBIZ_050202", "模板未配置!"),
    /**
     * 核心层-合同组件-通知风控方合同信息失败
     */
    CONTRACT_NOTIFY_FAILURE("GPBIZ_050203", "通知风控方合同信息失败!"),
    /**
     * 核心层-合同组件-生成合同文件失败
     */
    CREATE_CONTRACT_FAILURE("GPBIZ_050204", "生成合同失败！"),
    /**
     * 核心层-合同组件-电子签章失败
     */
    ELECTRONIC_SEAL_FAILURE("GPBIZ_050205", "电子签章失败!"),
    /**
     * 核心层-借据组件-借据还款金额扣减异常
     */
    IOU_REPAY_SUB_AMOUNT_ERROR("GPBIZ_050206", "借据还款金额扣减异常"),
    /**
     * 核心层-借据组件-借据还款仅支持全额方式
     */
    IOU_REPAY_ONLY_SUPPORT_FULL_AMOUNT("GPBIZ_050207", "借据还款仅支持全额方式"),
    /**
     * 核心层-还款组件-还款申请金额不正确
     */
    CORE_REPAY_APPALY_AMOUNT_ERROR("GPBIZ_050208", "还款申请金额不正确"),
    /**
     * 支用失败
     */
    LOAN_PAY_FAILURE("GPBIZ_050215", "支用失败。"),
    /**
     * 可用额度不足
     */
    AVAILABLE_BALANCE_NOT_ENOUGH("GPBIZ_050216", "支用额度超过可用额度，支用驳回。"),
    /**
     * 获取的key有异常值
     */
    USER_RESOURCE_TYPE_NOT_DEFINED("GPBIZ_050217", "获取的key有异常值"),
    /**
     * json转换不正确
     */
    USER_RESOURCE_JSON_TRANSFORM_FAILURE("GPBIZ_050218", "json转换不正确"),
    /**
     * 获取商户号有异常值
     */
    SUBUSER_NO_TRANSFORM_FAILURE("GPBIZ_050219", "授信信息提交用户号校验异常"),
    /**
     * 获取用户号有异常值
     */
    ENTERPRISE_CODE_TRANSFORM_FAILURE("GPBIZ_050220", "授信信息提交商户号校验异常"),
    /** 额度冻结失败 */
    FROST_BALANCE_FAILED("GPBIZ_050221","额度冻结失败"),
    /**
     * 获取信息扩展类型有异常
     */
    EXTENSION_TYPE_TRANSFORM_FAILURE("GPBIZ_050222", "授信信息提交扩展信息类型校验异常"),
    /**
     * 获取扩展信息有异常
     */
    EXTENSION_TRANSFORM_FAILURE("GPBIZ_050223", "授信信息提交扩展信息校验异常"),
    /**
     * 获取授信信息类型异常
     */
    EXTENSION_INFOS_TRANSFORM_FAILURE("GPBIZ_050224", "授信信息校验异常"),
    /**
     * 授信登记校验失败
     */
    CREDIT_INFO_REGISTER_CHECK_FAILURE("GPBIZ_050225", "授信信息登记校验异常"),

    /** 支用金额与收款总金额不相等 */
    LOAN_AMOUNT_NOT_MATCH_RECEIPT_AMOUNT("GPBIZ_050226","支用金额与收款总金额不一致"),

    /** 不在交易时间内 */
    TRX_TIME_ERROR("GPBIZ_050227","不在交易时间内"),

    /** 授信到期 */
    CREDIT_OUT_OF_DATE_ERROR("GPBIZ_050227","授信到期"),

    LOAN_AMOUNT_ERROR("GPBIZ_050228","支用金额异常"),

    QUERY_ERROR("GPBIZ_050229","查询异常"),

    CONTRACT_PARAM_ERROR("GPBIZ_050230","合同参数异常"),


    //*******************************公共层*************************************** //
    /**
     * 参数有误
     */
    PARAMETER_ERROR("GPBIZ_050301", "请求参数有误"),
    /** 属性拷贝异常 */
    BEAN_PROPERTIES_COPY_ERROR("GPBIZ_050302","属性拷贝异常"),
    /** 数据格式化异常 */
    DATA_FORMAT_ERROR("GPBIZ_050302","数据格式化异常"),
    OBJECT_TO_JSON_ERROR("GPBIZ_050303","对象转换JSON失败"),
    OBJECT_TO_JSON_NODE_ERROR("GPBIZ_050304","对象转换JsonNode失败"),
    PARSE_JOSN_ERROR("GPBIZ_050305","解析JSON失败"),
    FILE_PARSE_ERROR("GPBIZ_0503006","文件解析失败"),
    FILE_GENERATE_ERROR("GPBIZ_0503007","文件生成失败"),
    SIGN_ERROR("GPBIZ_050308","加签验签失败"),
    CIRCULAR_BEAN_ERROR("GPBIZ_050309","bean循环引用错误"),
    CLASS_MATCH_ERROR("GPBIZ_050310","类匹配错误"),

    //*******************************DAO层************************************** //
    /** 数据库异常 */
    DB_ERROR("GPBIZ_050400","数据库异常"),
    /**
     * 数据异常，不存在或者状态等异常
     */
    DATA_ERROR("GPBIZ_050401", "数据异常。"),
    DATA_EXISTS_ERROR("GPBIZ_050402", "数据已经存在"),
    DATA_REPEAT_SUBMIT("GPBIZ_050403", "重复提交"),
    /** 数据不存在 */
    DATA_NOT_FOUND("GP_050004", "数据不存在"),
    /** 插入数据失败 */
    INSERT_FAILURE("GPBIZ_050105", "数据插入失败"),
    /** 更新数据失败 */
    UPDATE_FAILURE("GPBIZ_050106", "数据更新失败"),
    /** 金额参数与交易流水金额不一致 */
    AMOUNT_INCONSISTENT("GPBIZ_050407", "金额参数与交易流水金额不一致"),
    /**
     * 状态异常
     */
    ERROR_STATUS("GPBIZ_050408", "状态异常。"),
    /** 没有更新到数据 */
    UPDATE_ZERO_ROW_ERROR("GPBIZ_050409","没有更新到数据"),


    //*******************************文件操作层********************************** //
    /**
     * 切换目录失败
     */
    SWITCH_DIRECTION_FAILURE("GPBIZ_050500", "Failed to switch directory."),
    /**
     * 创建目录失败
     */
    CREATE_DIRECTION_FAILURE("GPBIZ_050501", "Failed to create directory."),
    /**
     * 线程池销毁对象失败
     */
    INVALIDATE_POOL_OBJECT_FAILURE("GPBIZ_050502", "Fail to destroy pool object."),
    /**
     * 输入流关闭失败
     */
    CLOSE_INPUTSTREAM_FAILURE("GPBIZ_050503", "Fail to close inputstream."),
    /**
     * 输出流关闭失败
     */
    CLOSE_OUTPUTSTREAM_FAILURE("GPBIZ_050504", "Close OutputStream failed."),
    /**
     * 文件上传失败
     */
    UPLOAD_FILE_FAILURE("GPBIZ_050505", "Upload file failed!"),
    /**
     * 文件下载失败
     */
    DOWNLOAD_FILE_FAILURE("GPBIZ_050506", "Download file failed!"),
    /**
     * 删除文件失败
     */
    DELETE_FILE_FAILURE("GPBIZ_050507", "Delete file failed!"),
    /**
     * 获取FTPClient对象失败
     */
    GET_FTPCLIENT_FAILURE("GPBIZ_050508", "Get FTPClient object failed."),
    /**
     * 获取ChannelSftp对象失败
     */
    GET_CHANNELSFTP_FAILURE("GPBIZ_050509", "Get ChannelSftp object failed."),
    /**
     * 文件大小超过限制
     */
    EXCEED_FILE_SIZE_LIMIT("GPBIZ_050510", "File size exceed the file size limit."),
    /**
     * 文件大小超过限制
     */
    UNSUPPORTED_FILE_FORMAT("GPBIZ_050511", "Unsupported file format."),


    //*******************************外部调用层********************************** //
    HTTP_CALL_TIME_OUT_ERROR("GPBIZ_060OOO","接口调用超时"),
    HTTP_CALL_ERROR("GPBIZ_060OO1","接口调用错误"),
    HTTP_CALL_UNKNOWN_ERROR("GPBIZ_060OO2","未知异常"),
    HTTP_RESPONSE_ERROR("GPBIZ_060OO3","接口请求返回失败");

    /**
     * 编码CODE
     */
    private String code;
    /**
     * 编码MSG
     */
    private String msg;
}
