package org.tp.mix;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 未验证....
 **/
public class MybatisPlusGeneratorConfig {
    public static void main(String[] args) {
        FastAutoGenerator.create("url", "username", "password")
                .globalConfig(builder -> {
                    builder.author("gunten") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("org.tp.mix") // 设置父包名
                            .moduleName("dal") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "org/tp/mix/dal")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("city","country","countrylanguage") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
