package com.kindstar.hst.cw;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@MapperScan("com.kindstar.hst.cw.mapper")
@EnableEurekaClient
public class KindstarHstCvProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(KindstarHstCvProviderApp.class,args);
    }
}
