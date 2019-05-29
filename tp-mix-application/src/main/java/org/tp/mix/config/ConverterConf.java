package org.tp.mix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tp.mix.converter.ResultModelWrapperConverter;

/**
 * 配置自定义消息转换器配置类
 * @version
 */
@Configuration
public class ConverterConf {

    @Bean
    public ResultModelWrapperConverter getResultModelWrapperConverter() {
        return new ResultModelWrapperConverter();
    }

}
