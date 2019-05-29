package org.tp.annotation.autolog;


import org.tp.annotation.sensitive.SensitiveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统自动打印方法日志注解
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLog {

    /**
     * 日志级别， 默认INFO
     *
     */
    LogLevelEnum level() default LogLevelEnum.INFO;

    /**
     * 方法返回类型若是String，且需要脱敏时，加入其脱敏类型。
     * 默认为空
     *
     */
    SensitiveType[] returnSensitiveType() default {};

    /**
     * 是否打印方法返回结果
     * 默认为打印
     *
     */
    boolean isPrintReturnResult() default true;
}
