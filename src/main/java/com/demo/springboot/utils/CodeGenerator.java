package com.demo.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author LISHANSHAN
 * @ClassName CodeGenerator
 * @Description MyBatis-Plus代码生成器
 * @date 2022/05/2022/5/31 22:37
 */

public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }
    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/demo?serverTimezone=GMT%2b8&characterEncoding=UTF-8&userSSL=false",
                        "root", "root12345")
                .globalConfig(builder -> {
                    builder.author("LISHANSHAN") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            // 目录要设好，这样代码可以很顺利地放到对应的位置
                            .outputDir("E:\\IDEA_coding\\springboot\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.demo.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\IDEA_coding\\springboot\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    // 开启驼峰转连字符，也就是请求路径；开启生成@RestController控制器，Rest风格JSON，不然生成的是Controller，就返回视图名称
                    builder.controllerBuilder().enableHyphenStyle()
                                    .enableRestStyle();
                    builder.addInclude("sys_menu") // 设置需要生成的表名
                            .addTablePrefix("sys_", "t_"); // 设置过滤表前缀
                })
                // .templateEngine(new VelocityTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
