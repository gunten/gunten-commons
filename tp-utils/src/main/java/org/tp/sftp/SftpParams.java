package org.tp.sftp;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * SFTP服务器基本配置信息
 * @version
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString(exclude = { "userName", "safeKey" })
public class SftpParams {
    /** SFTP服务器主机 */
    @NonNull
    private String sftpServerHost;
    /** SFTP服务器端口 */
    @NonNull
    private Integer sftpServerPort;
    /** SFTP用户名 */
    @NonNull
    private String userName;
    /** SFTP密码 */
    @NonNull
    private String safeKey;
    /** SFTP操作类型,1=upload 2=download */
    @NonNull
    private Integer transType;
    /** 路径 */
    private String sftpDirectory;
}
