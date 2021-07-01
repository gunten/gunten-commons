package org.tp;

import org.springframework.context.annotation.Import;
import org.tp.autoconfiguration.SmsAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2021/7/1
 */


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SmsAutoConfiguration.class})
public @interface EnableSms {
}
