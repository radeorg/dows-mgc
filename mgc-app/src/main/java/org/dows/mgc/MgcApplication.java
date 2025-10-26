package org.dows.mgc;

import io.github.cdimascio.dotenv.Dotenv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;


//启用缓存
@EnableCaching
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
//可在业务逻辑中获取当前的代理对象
@MapperScan("org.dows.mgc.mapper")
@EnableScheduling
public class MgcApplication {

    public static void main(String[] args) {

        String property = System.getProperty("user.home");
        Dotenv dotenv = Dotenv.configure()
                .directory(property+ File.separator+ "env") // 指定 env 目录路径
                .ignoreIfMissing()
                .load();
        // 将 .env 文件中的键值对设为系统属性
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(MgcApplication.class, args);
    }

}