package org.tp.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tp.enums.FtpTransTypeEnum;
import org.tp.exception.BizCodeMsgEnum;
import org.tp.exception.MyUtilsException;

import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * SFTP操作模板类
 */
@Slf4j
@Data
@Accessors(chain = true)
public class SftpTemplate {

    /**
     * ChannelSftp线程池
     */
    @Autowired
    private ChannelSftpPool channelSftpPool;
    /**
     * 上传文件大小限制
     */
    @Value("${sftp.filesize}")
    private String fileSize;
    /**
     * 上传文件格式限制
     */
    @Value("${sftp.fileformat}")
    private String fileformat;

    /** SFTP服务器配置信息 **/
    private SftpParams sftpParams;

    /**
     * 校验上传文件大小是否在允许范围内
     *
     * @param filesize 上传的文件大小
     * @return true=允许 false=不允许
     */
    public boolean verifyFilesize(Integer filesize) {
        boolean isPermit = false;
        try {
            String limitSize = fileSize;
            if (StringUtils.isBlank(limitSize)) {
                return true;
            }
            if (StringUtils.isNotBlank(limitSize) && Pattern.compile("[0-9]*")
                    .matcher(limitSize)
                    .matches() && (filesize <= (Integer.parseInt(limitSize) * 1024 * 1024))) {
                isPermit = true;
            }
        } catch (Exception e) {
            log.warn("File size exceed the file size limit!", e);
        }
        return isPermit;
    }

    /**
     * 校验文件格式是否允许上传
     *
     * @param fileName
     * @return true=允许 false=不允许
     */
    public boolean filterFile(String fileName) {
        boolean isPermit = false;
        try {
            if (StringUtils.isNotBlank(fileformat) && StringUtils.isNotBlank(fileName) && (fileformat.indexOf(
                    fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()) != -1)) {
                isPermit = true;
            }
        } catch (Exception e) {
            log.warn("Unsupported file format!", e);
        }
        return isPermit;
    }

    /**
     * 上传文件
     * <p>如果sftp服务器存在相同文件名，则会覆盖掉原有的文件</p>
     *
     * @param directory      上传目录(可配置为null，此时使用saveFilename绝对路径保存)
     * @param remoteFilename 上传后文件名称(可为绝对路径)
     * @param uploadFilePath 本地待上传文件名称，绝对路径
     * @return true表示上传成功，false表示上传失败
     */
    public boolean upload(String directory, String remoteFilename, String uploadFilePath) {
        ChannelSftp channelSftp = getChannelSftp(channelSftpPool, sftpParams);
        File file = new File(uploadFilePath);
        try (InputStream input = new FileInputStream(file)) {
            channelSftp.cd(sftpParams.getSftpDirectory());
            if (StringUtils.isNotBlank(uploadFilePath) && !filterFile(uploadFilePath)) {
                log.warn("Unsupported file format,uploadFilePath:{}!", uploadFilePath);
                return false;
            }
            // 检查目录是否存在，不存在就创建目录
            if (StringUtils.isNotBlank(directory) && !isDirExist(directory, channelSftp)) {
                mkdirs(directory, channelSftp);
            }
            // 检验文件大小
            if (verifyFilesize(input.available())) {
                if (StringUtils.isNotBlank(remoteFilename)) {
                    channelSftp.put(input, remoteFilename);
                } else {
                    channelSftp.put(input, file.getName());
                }
                log.debug("Upload file successed,uploadFilePath:{},sftpParams:{}.", uploadFilePath, sftpParams);
            } else {
                log.debug("File size exceed the file size limit!File limit {}M。", fileSize);
            }
        } catch (Exception e) {
            invalidateObject(sftpParams, channelSftp);
            log.warn("Upload file failed,uploadFilePath:{},sftpParams:{}.", uploadFilePath, sftpParams, e);
            return false;
        } finally {
            if (channelSftp != null) {
                returnChannelSftp(channelSftpPool, sftpParams, channelSftp);
            }
        }
        return true;
    }

    /**
     * 上传文件
     * <p>如果sftp服务器存在相同文件名，则会覆盖掉原有的文件</p>
     *
     * @param directory      上传目录(可配置为null，此时使用saveFilename绝对路径保存)
     * @param remoteFilename 上传后文件名称(可为绝对路径)
     * @param input          输入流
     * @return true表示上传成功，false表示上传失败
     */
    public boolean uploadByStream(String directory, String remoteFilename, InputStream input) {
        ChannelSftp channelSftp = getChannelSftp(channelSftpPool, sftpParams);
        try {
            channelSftp.cd(sftpParams.getSftpDirectory());
            if (StringUtils.isNotBlank(directory)) {
                // 检查目录是否存在，不存在就创建目录
                if (!isDirExist(directory, channelSftp)) {
                    mkdirs(directory, channelSftp);
                } else {
                    channelSftp.cd(directory);
                }
            }
            // 检验文件大小
            if (verifyFilesize(input.available())) {
                if (StringUtils.isNotBlank(remoteFilename)) {
                    channelSftp.put(input, remoteFilename);
                }
            } else {
                log.debug("File size exceed the file size limit!File limit {}M。", fileSize);
            }
        } catch (SftpException e) {
            log.warn(e.toString(), e);
        } catch (Exception e) {
            invalidateObject(sftpParams, channelSftp);
            log.warn("Upload file failed,sftpParams:{}.", sftpParams, e);
            return false;
        } finally {
            if (channelSftp != null) {
                returnChannelSftp(channelSftpPool, sftpParams, channelSftp);
            }
        }
        return true;
    }

    /**
     * 下载附件
     *
     * @param remoteFilePath 待下载的文件
     * @param outputStream   文件待放入的OutputStream输出流
     * @return true|false
     */
    public boolean downloadFile(String remoteFilePath, OutputStream outputStream) {
        boolean result = true;
        InputStream is = null;
        ChannelSftp channelSftp = getChannelSftp(channelSftpPool, sftpParams);
        try {
            channelSftp.cd(sftpParams.getSftpDirectory());
            is = channelSftp.get(new String(remoteFilePath.getBytes("UTF-8"), "iso-8859-1"));
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            invalidateObject(sftpParams, channelSftp);
            result = false;
            log.warn("Download file failed,remoteFilePath:{},sftpParams:{}.", remoteFilePath, sftpParams, e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.warn("Fail to close inputstream.", e);
                }
            }
            // return to object pool
            if (channelSftp != null) {
                returnChannelSftp(channelSftpPool, sftpParams, channelSftp);
            }
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param deleteFile 待删除的文件完整路径
     * @return true|false
     */
    public boolean delete(String deleteFile) {
        ChannelSftp channelSftp = getChannelSftp(channelSftpPool, sftpParams);
        try {
            channelSftp.cd(sftpParams.getSftpDirectory());
            channelSftp.rm(deleteFile);
            log.debug("Delete sftp file succeed,file path {}.", deleteFile);
            return true;
        } catch (Exception e) {
            invalidateObject(sftpParams, channelSftp);
            log.warn("Delete sftp file failed,file path {}.", deleteFile, e);
            throw new MyUtilsException(e, BizCodeMsgEnum.DELETE_FILE_FAILURE.getCode(), e.getMessage(),
                    this.getClass());
        } finally {
            if (channelSftp != null) {
                returnChannelSftp(channelSftpPool, sftpParams, channelSftp);
            }
        }
    }



    /**
     * 取得ChannelSftp
     * @param pool
     * @param ftpConfig
     * @return ChannelSftp
     */
    private ChannelSftp getChannelSftp(ChannelSftpPool pool, SftpParams ftpConfig) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = pool.borrowObject(ftpConfig);
        } catch (Exception e) {
            log.warn("Get ChannelSftp object failed,ftpConfig:{}", ftpConfig, e);
            throw new MyUtilsException(BizCodeMsgEnum.GET_CHANNELSFTP_FAILURE.getCode(),
                    BizCodeMsgEnum.GET_CHANNELSFTP_FAILURE.getMsg(), this.getClass());
        }
        return channelSftp;
    }


    private void invalidateObject(SftpParams sftpParams, ChannelSftp channelSftp) {
        try {
            channelSftpPool.invalidateObject(sftpParams, channelSftp);
        } catch (Exception e1) {
            log.warn("Fail to destroy pool object.", e1);
            throw new MyUtilsException(BizCodeMsgEnum.INVALIDATE_POOL_OBJECT_FAILURE.getCode(),
                    BizCodeMsgEnum.INVALIDATE_POOL_OBJECT_FAILURE.getMsg(), this.getClass());
        }
    }

    /**
     * 返回ChannelSftpPool
     *
     * @param pool
     * @param ftpConfig
     * @param channelSftp
     */
    private void returnChannelSftp(ChannelSftpPool pool, SftpParams ftpConfig, ChannelSftp channelSftp) {
        pool.returnObject(ftpConfig, channelSftp);
    }

    /**
     * 判断文件夹是否存在
     *
     * @param path 待验证的文件路径
     * @param sftp
     * @return true|false
     */
    private boolean isDirExist(String path, ChannelSftp sftp) {
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("no such file")) {
                return false;
            }
        }
        return false;
    }

    /**
     * 建立多目录文件夹,创建成功后直接切换
     *
     * @param dirPath 路径
     * @param sftp
     * @return true|false
     */
    private void mkdirs(String dirPath, ChannelSftp sftp) {
        StringTokenizer st = new StringTokenizer(dirPath, "/");
        try {
            String path;
            while (st.hasMoreTokens()) {
                path = st.nextToken();
                try {
                    if (!isDirExist(path, sftp)){
                        sftp.mkdir(path);
                    }
                    sftp.cd(path);
                } catch (Exception e) {
                    log.warn("Failed to create directory,directory:{}", dirPath, e);
                    throw new MyUtilsException(BizCodeMsgEnum.CREATE_DIRECTION_FAILURE.getCode(),
                            BizCodeMsgEnum.CREATE_DIRECTION_FAILURE.getMsg(), this.getClass());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to create directory,directory:{}", dirPath, e);
            throw new MyUtilsException(BizCodeMsgEnum.CREATE_DIRECTION_FAILURE.getCode(),
                    BizCodeMsgEnum.CREATE_DIRECTION_FAILURE.getMsg(), this.getClass());
        }
    }

}
