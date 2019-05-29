package org.tp.poi.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @des Excel 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    /** 对应的列名称 */
    String name() default "";

    /** 列序号 */
    int index();

    /** 字段类型对应的格式 */
    String format() default "";

    /** 是否自定义类 */
    boolean isDIYClass() default false;

    /** 校验是否可以为空 */
    boolean nullable() default true;
}
