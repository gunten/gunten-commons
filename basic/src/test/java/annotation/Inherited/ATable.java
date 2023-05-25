package annotation.Inherited;

import java.lang.annotation.*;

/**
 * 2023/6/14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ATable {
    String name() default "";
}

