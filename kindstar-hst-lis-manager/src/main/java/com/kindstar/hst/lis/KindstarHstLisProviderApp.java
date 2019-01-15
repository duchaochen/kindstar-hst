package com.kindstar.hst.lis;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 */
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@MapperScan("com.kindstar.hst.lis.mapper")
@EnableEurekaClient
public class KindstarHstLisProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(KindstarHstLisProviderApp.class,args);
    }
}
