//package org.tp.mix.controler;
//
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.tp.mix.dal.model.City;
//import org.tp.mix.dal.service.ICityService;
//
///**
// * <p>
// *  前端控制器
// * </p>
// *
// * @author
// * @since 2021-06-26
// */
//@RestController
//@RequestMapping("/city")
//public class CityController {
//
//    @Autowired
//    ICityService cityService;
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<City> city(@PathVariable("id")Integer id){
//        City city = cityService.getById(id);
//        return ResponseEntity.ok(city);
//    }
//
//    @GetMapping("/redis/{id}")
//    public ResponseEntity<City> cityRedis(@PathVariable("id")Integer id){
//        String city=(String)redisTemplate.opsForValue().get("CITY:"+id);
//        return ResponseEntity.ok(JSON.parseObject(city,City.class));
//    }
//}
