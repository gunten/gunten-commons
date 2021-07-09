package org.tp;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * springboot2.0不能与activiti6.0.0直接集成使用，因为activiti6.0.0出来的时候springboot2.0还没有出来，
 * activiti6.0.0 支持springboot1.2.6以上，2.0.0以下的版本。
 */
@SpringBootApplication(scanBasePackages = "org.tp", exclude = SecurityAutoConfiguration.class)
@MapperScan("org.tp.transactional.annotation.mapper")
@EnableBatchProcessing
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
