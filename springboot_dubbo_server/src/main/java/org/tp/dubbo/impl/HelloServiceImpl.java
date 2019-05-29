package org.tp.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.tp.dubbo.HelloService;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/1/10
 */
@Service(version = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String SayHello(String name) {
        return "Hello , "+name;
    }
}
