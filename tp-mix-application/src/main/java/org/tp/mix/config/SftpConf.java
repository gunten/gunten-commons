//package org.tp.mix.config;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.tp.enums.FtpTransTypeEnum;
//import org.tp.sftp.SftpParams;
//import org.tp.sftp.SftpTemplate;
//
///**
// * SftpConfig实例注入配置管理类
// *
// * @since 2019/03/05
// */
//@Configuration
//public class SftpConf {
//
//    @Bean("uploadSftpParams")
//    @ConfigurationProperties(prefix = "sftp")
//    public SftpParams getUploadSftpParams() {
//        return new SftpParams().setTransType(FtpTransTypeEnum.UPLOAD.getCode());
//    }
//
//    @Bean
//    public SftpTemplate uploadSftpTemplate(@Autowired @Qualifier("uploadSftpParams") SftpParams sftpParams) {
//        return new SftpTemplate().setSftpParams(sftpParams);
//    }
//
//
//    @Bean
//    public SftpTemplate downloadSftpTemplate(@Autowired @Qualifier("uploadSftpParams")SftpParams sftpParams) {
//        SftpParams target = new SftpParams();
//        BeanUtils.copyProperties(sftpParams,target);
//        target.setTransType(FtpTransTypeEnum.DOWNLOAD.getCode());
//        return new SftpTemplate().setSftpParams(target);
//    }
//
//
//}
