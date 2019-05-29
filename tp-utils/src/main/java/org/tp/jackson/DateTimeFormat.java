package org.tp.jackson;

/**
 * {@link DateTimeFormat}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see DateTimeFormat
 * 2018/12/25
 */
public class DateTimeFormat {

    /**
     * yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String DATE_PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_LONG_COMPRESS = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_SLASH = "yyyy/MM/dd hh:mm:ss";
    public static final String DATE_PATTERN_SHORT = "yyyy-MM-dd";
    public static final String DATE_PATTERN_SHORT_COMPRESS = "yyyyMMdd";
    public static final String TIME_PATTERN = "HH:mm:ss";


    public static final long ONE_DAY_MILLISECOND = 24L * 60 * 60 * 1000;
    public static final long ONE_HOUR_MILLISECOND = 60L * 60 * 1000;
    public static final long ONE_DAY_SEC = 24L * 60 * 60;
}
