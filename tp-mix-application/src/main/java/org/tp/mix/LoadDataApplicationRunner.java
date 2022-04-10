package org.tp.mix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.tp.mix.dal.model.City;
import org.tp.mix.dal.service.ICityService;

import java.util.List;


/**
 * 启动 load缓存
 **/
@Slf4j
//@Component
public class LoadDataApplicationRunner implements ApplicationRunner {

    @Autowired
    ICityService cityService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=========begin load city data to Redis===========");
        List<City> cityList=cityService.list();
        cityList.parallelStream().forEach(city -> {
            stringRedisTemplate.opsForValue().set("CITY:"+city.getId(), JSON.toJSONString(city));
        });
        log.info("=========finish load city data to Redis===========");
    }
}
