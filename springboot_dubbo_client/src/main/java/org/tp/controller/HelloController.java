package org.tp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tp.dubbo.HelloService;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/1/10
 */
@RestController
public class HelloController {

    @Reference(version = "1.0.0")
    HelloService helloService;

    @GetMapping("sayHello")
    public String sayHello(String name){
        return helloService.SayHello(name);
    }

}
