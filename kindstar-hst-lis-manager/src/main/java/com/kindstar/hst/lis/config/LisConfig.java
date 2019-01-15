package com.kindstar.hst.lis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

@Configuration
public class LisConfig {

    /**
     * redis操作字符串
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        Jackson2JsonEncoder jsonEncoder = new  Jackson2JsonEncoder();
        ObjectMapper objectMapper = jsonEncoder.getObjectMapper();
        return objectMapper;
    }
}
