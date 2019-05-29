package org.tp.annotation.sensitive;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性字段脱敏字段注解
 * 针对【对象属性】 和 【方法基本类型参数 】字段注解，不做处理，通过使用场景工具抓取判断使用
 * 实例：@SensitiveInfo(type = SensitiveType.ID_CARD)
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveInfo {
    SensitiveType type();
}
