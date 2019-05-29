package org.tp.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

/**
 * 工具处理异常类
 * TODO 强耦合业务Enum
 */

@Slf4j
public class MyUtilsException extends ApplicationException {

    /**
     * 工具验证逻辑异常，只需要使用错误码和错误信息
     *
     * @param sysCodeMsgEnum 错误枚举类型
     * @param zlass          异常出现的类
     */
    public MyUtilsException(SysCodeMsgEnum sysCodeMsgEnum, Class<?> zlass) {
        super(sysCodeMsgEnum.getCode(), sysCodeMsgEnum.getMsg());
        LoggerFactory.getLogger(zlass).warn("{}{}", sysCodeMsgEnum.getCode(), sysCodeMsgEnum.getCode());
    }

    /**
     * 工具验证逻辑异常，只需要使用错误码和错误信息
     *
     * @param bizCodeMsgEnum 错误枚举类型
     * @param zlass          异常出现的类
     */
    public MyUtilsException(BizCodeMsgEnum bizCodeMsgEnum, Class<?> zlass) {
        super(bizCodeMsgEnum.getCode(), bizCodeMsgEnum.getMsg());
        LoggerFactory.getLogger(zlass).warn("{}{}", bizCodeMsgEnum.getCode(), bizCodeMsgEnum.getCode());
    }

    /**
     * 工具验证逻辑异常，只需要使用错误码和错误信息
     *
     * @param errorCode 错误编码
     * @param errorMsg  错误消息
     * @param zlass     异常出现的类
     */
    public MyUtilsException(String errorCode, String errorMsg, Class<?> zlass) {
        super(errorCode, errorMsg);
        LoggerFactory.getLogger(zlass).warn("{}{}", errorCode, errorMsg);
    }

    /**
     * 工具技术执行异常需要把异常传递（登记日志）
     *
     * @param cause          异常消息
     * @param bizCodeMsgEnum 错误枚举类型
     * @param zlass          异常出现的类
     */
    public MyUtilsException(Throwable cause, BizCodeMsgEnum bizCodeMsgEnum, Class<?> zlass) {
        super(cause, bizCodeMsgEnum.getCode(), bizCodeMsgEnum.getMsg());
        LoggerFactory.getLogger(zlass).warn("{}{}{}{}", bizCodeMsgEnum.getCode(), bizCodeMsgEnum.getMsg(),
                cause.getMessage(), cause);
    }

    /**
     * 工具技术执行异常需要把异常传递（登记日志）
     *
     * @param cause     异常消息
     * @param errorCode 错误编码
     * @param errorMsg  错误消息
     * @param zlass     异常出现的类
     */
    public MyUtilsException(Throwable cause, String errorCode, String errorMsg, Class<?> zlass) {
        super(cause, errorCode, errorMsg);
        LoggerFactory.getLogger(zlass).warn("{}{}{}{}", errorCode, errorMsg, cause.getMessage(), cause);
    }
}
