package org.tp.sftp.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.tp.sftp.ChannelSftpPool;
import org.tp.sftp.ChannelSftpPoolParams;
import org.tp.sftp.ChannelSftpPooledFactory;

/**
 * @version
 */
@Configuration
public class SftpContext {

    /**
     * 
     * @return ChannelSftpPooledFactory
     */
    @Primary
    @Bean
    ChannelSftpPooledFactory getChannelSftpPooledFactory() {
        return new ChannelSftpPooledFactory();
    }

    /**
     * 
     * @param factory
     * @param config
     * @return ChannelSftpPool
     */
    @Bean
    @Autowired
    ChannelSftpPool getChannelSftpPool( ChannelSftpPooledFactory factory,  ChannelSftpPoolParams config) {
        return new ChannelSftpPool(factory, config);
    }

}
