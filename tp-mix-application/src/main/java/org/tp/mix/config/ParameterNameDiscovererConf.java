package org.tp.mix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * 配置获取构造函数方法以及方法的实际参数名称发现接口的配置类
 */
@Configuration
public class ParameterNameDiscovererConf {

    @Bean
    public ParameterNameDiscoverer getParameterNameDiscoverer(){
        return new DefaultParameterNameDiscoverer();
    }

}
