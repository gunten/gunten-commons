package org.tp.sftp;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * FTPClient线程池配置信息
 * @version
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "sftp-client-pool")
public class ChannelSftpPoolParams extends GenericKeyedObjectPoolConfig {

    /**
     * 构造函数
     */
    public ChannelSftpPoolParams() {
        setTestWhileIdle(this.getTestWhileIdle());
        setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        setTestOnBorrow(this.getTestOnBorrow());
    }

}
