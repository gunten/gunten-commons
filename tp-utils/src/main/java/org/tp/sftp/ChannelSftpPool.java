package org.tp.sftp;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.tp.sftp.ChannelSftpPoolParams;
import org.tp.sftp.ChannelSftpPooledFactory;
import org.tp.sftp.SftpParams;

public class ChannelSftpPool extends GenericKeyedObjectPool<SftpParams, ChannelSftp> {

    /**
     * 构造函数
     *
     * @param factory
     * @param config
     */
    public ChannelSftpPool(ChannelSftpPooledFactory factory, ChannelSftpPoolParams config) {
        super(factory, config);
    }

}
