package org.tp.autoconfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tp.AliyunSmsSenderImpl;
import org.tp.SmsProperties;
import org.tp.TencentSmsSenderImpl;


/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2021/7/1
 */
@EnableConfigurationProperties(value = SmsProperties.class)
@Configuration
public class SmsAutoConfiguration  {
    /**
     *  阿里云发送短信的实现类
     * @param smsProperties
     * @return
     */
    @Bean
    public AliyunSmsSenderImpl aliYunSmsSender(SmsProperties smsProperties){
        return new AliyunSmsSenderImpl(smsProperties.getAliyun());
    }
    /**
     * 腾讯云发送短信的实现类
     * @param smsProperties
     * @return
     */
    @Bean
    public TencentSmsSenderImpl tencentSmsSender(SmsProperties smsProperties){
        return new TencentSmsSenderImpl(smsProperties.getTencent());
    }
}
