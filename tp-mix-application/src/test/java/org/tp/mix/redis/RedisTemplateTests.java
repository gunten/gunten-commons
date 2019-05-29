package org.tp.mix.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.tp.mix.vo.User;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


/**
 * 更多例子
 * https://github.com/lettuce-io/lettuce-core/tree/5.1.3.RELEASE/src/test/java/io/lettuce/examples
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTests {

    private static final Logger log = LoggerFactory.getLogger(RedisTemplateTests.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;


    /*
        opsForValue： 对应 String（字符串）
        opsForZSet： 对应 ZSet（有序集合）
        opsForHash： 对应 Hash（哈希）
        opsForList： 对应 List（列表）
        opsForSet： 对应 Set（集合）
        opsForGeo： 对应 GEO（地理位置）
     */
    @Test
    public void getTest() {
        //测试线程安全
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        stringRedisTemplate.delete("kk");
        stringRedisTemplate.opsForValue().set("bingfa","0");
        IntStream.range(0, 1000).forEach(i ->
                executorService.execute(() -> stringRedisTemplate.opsForValue().increment("bingfa", 1))
        );
        //这里有坑，返回结果好像比较慢
        log.info("[并发结果] - [{}]", stringRedisTemplate.opsForValue().get("bingfa"));
        log.info("[并发结果] - [{}]", stringRedisTemplate.opsForValue().get("bingfa"));
        stringRedisTemplate.opsForValue().set("k1", "v1");
        final String k1 = stringRedisTemplate.opsForValue().get("k1");
        log.info("[字符缓存结果] - [{}]", k1);

        // 以下只演示整合，具体Redis命令可以参考官方文档，Spring Data Redis 只是改了个名字而已，Redis支持的命令它都支持
        String key = "battcn:user:1";
        redisCacheTemplate.opsForValue().set(key, new User(1L, "u1", 18, "u1@163.com"));
        // 对应 String（字符串）
        final User user = (User) redisCacheTemplate.opsForValue().get(key);
        log.info("[对象缓存结果] - [{}]", user);
    }

}
