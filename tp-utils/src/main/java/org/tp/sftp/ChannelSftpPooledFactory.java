package org.tp.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * SFTP ChannelSftp池化工厂类
 * @version
 */
@Slf4j
public class ChannelSftpPooledFactory implements KeyedPooledObjectFactory<SftpParams, ChannelSftp> {

    /* (non-Javadoc)
     * @see org.apache.commons.pool2.KeyedPooledObjectFactory#makeObject(java.lang.Object)
     */
    @Override
    public PooledObject<ChannelSftp> makeObject(SftpParams key) throws Exception {
        JSch jsch = new JSch();
        // 根据用户名，主机ip，端口获取一个Session对象
        Session session = jsch.getSession(key.getUserName(), key.getSftpServerHost(), key.getSftpServerPort());
        // 设置密码
        session.setPassword(key.getSafeKey());
        // 为Session对象设置properties
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        // 通过Session建立链接
        session.connect();
        // 打开SFTP通道
        Channel channel = session.openChannel("sftp");
        // 建立SFTP通道的连接
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp) channel;
        Field f1 = channelSftp.getClass().getDeclaredField("server_version");
        f1.setAccessible(true);
        f1.set(channelSftp, 2);
        channelSftp.setFilenameEncoding("GBK");
        return new DefaultPooledObject<>(channelSftp);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.pool2.KeyedPooledObjectFactory#destroyObject(java.lang.Object, org.apache.commons.pool2.PooledObject)
     */
    @Override
    public void destroyObject(SftpParams key, PooledObject<ChannelSftp> p) throws Exception {
        log.info("Ready to destoy client object.");
        ChannelSftp channelSftp = p.getObject();
        if(channelSftp != null && channelSftp.isConnected()) {
            Session sshSession = channelSftp.getSession();
            if (null != sshSession && sshSession.isConnected()) {
                sshSession.disconnect();
            }
            channelSftp.quit();
            channelSftp.disconnect();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.commons.pool2.KeyedPooledObjectFactory#validateObject(java.lang.Object, org.apache.commons.pool2.PooledObject)
     */
    @Override
    public boolean validateObject(SftpParams key, PooledObject<ChannelSftp> p) {
        ChannelSftp channelSftp = p.getObject();
        if(!channelSftp.isConnected()) {
            log.info("连接失效 : " + key + ", 连接状态: " + false);
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.pool2.KeyedPooledObjectFactory#activateObject(java.lang.Object, org.apache.commons.pool2.PooledObject)
     */
    @Override
    public void activateObject(SftpParams key, PooledObject<ChannelSftp> p) throws Exception {
        // Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.commons.pool2.KeyedPooledObjectFactory#passivateObject(java.lang.Object, org.apache.commons.pool2.PooledObject)
     */
    @Override
    public void passivateObject(SftpParams key, PooledObject<ChannelSftp> p) throws Exception {
        // Auto-generated method stub
        
    }

}
