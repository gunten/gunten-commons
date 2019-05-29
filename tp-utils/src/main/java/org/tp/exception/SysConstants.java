package org.tp.exception;


import java.nio.charset.Charset;

/**
 * 系统基本常量
 *
 */
public class SysConstants {

    private SysConstants(){

    }

    /** 系统默认创建人 */
    public static final String DEFAULT_CREATOR = "SYSTEM";
    /** 系统默认修改人 */
    public static final String DEFAULT_MODIFIER = "SYSTEM";
    /** 系统默认编码 */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    /** 系统默认资源项名称 */
    public static final String RESOURCE = "resource";
    /** 系统默认分页大小最大值 */
    public static final Integer DEFAULT_PAGE_SIZE = 200;
    /** HTTP请求失败最大重试次数 */
    public static final Integer MAX_RETRY_TIMES = 1;

    public static final String JSON_STRING_EMPTY_DESC = "JSON字符串不能为空";

    public static final String CODE = "code";

    /** 支用结果查询定时时间 */
    public static final String LOAN_RESULT_QUERY_CRON = "0/20 * * * * ? *";
    /** 授信结果查询定时时间 */
    public static final String FSP_CREDIT_RESULT_QUERY_CRON = "0 0/10 * * * ? *";

    /** JSON文件后缀名 */
    public static final String FILE_JSON_SUFFIX = ".json";
    /** ZIP文件后缀名 */
    public static final String FILE_ZIP_SUFFIX = ".zip";
    /** CSV文件后缀名 */
    public static final String FILE_CSV_SUFFIX = ".csv";

}
