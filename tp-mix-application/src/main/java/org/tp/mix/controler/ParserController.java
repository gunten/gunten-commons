package org.tp.mix.controler;

import gunten.spi.example.ParserManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 *  SPI 测试入口
 **/
@RestController
    @RequestMapping("/parser")
public class ParserController {



    @GetMapping("/{type}")
    public String parser(@PathVariable("type") String type){
        try {
            return ParserManager.getParser(type).parse(new File(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "不支持该种解析方式";
    }
}
