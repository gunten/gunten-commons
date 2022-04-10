package org.tp.mix.controler;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 **/
@RestController
public class RedissonController {
    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/redisson")
    public String get(){
        redissonClient.getBucket("customer").set("a new val");
        /**
         * 反序列化 会有个问题， 默认带\"
         */
        Object value = redissonClient.getBucket("customer").get();
        return value.toString();
    }

}
