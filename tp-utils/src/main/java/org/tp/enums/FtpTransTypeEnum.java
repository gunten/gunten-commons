package org.tp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * FTP操作类型枚举
 * </p>
 *
 * @since 2018/12/14
 */
@Getter
@AllArgsConstructor
public enum FtpTransTypeEnum {

    UPLOAD(1, "上传文件"),

    DOWNLOAD(2, "下载文件");

    /**
     * 操作类型编码
     */
    private Integer code;

    /**
     * 操作类型描述
     */
    private String desc;
}
