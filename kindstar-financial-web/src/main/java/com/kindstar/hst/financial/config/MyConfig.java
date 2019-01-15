package com.kindstar.hst.financial.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
    /**
     * 后端调用http请求类
     * @LoadBalanced: 配置负载均衡
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
    /**
     * 创建一个操作redis将对象转成字符串的对象
     * @param redisConnectionFactory
     * @return
     */
    @Bean(name="redisTemplate")
    public RedisTemplate<String,Object> myRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
//    @Bean
//    public RedisTemplate<Object,PatItemResult_JZBBView> myRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object,PatItemResult_JZBBView> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        Jackson2JsonRedisSerializer<PatItemResult_JZBBView> redisSerializer = new Jackson2JsonRedisSerializer<>(PatItemResult_JZBBView.class);
//        redisTemplate.setDefaultSerializer(redisSerializer);
//        return redisTemplate;
//    }

}
