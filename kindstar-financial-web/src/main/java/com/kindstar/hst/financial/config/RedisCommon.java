package com.kindstar.hst.financial.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class RedisCommon {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private ZSetOperations<String,String> saveKey;
    private HashOperations<String,String,String> saveValue;

    public RedisCommon() {
//        this.saveKey = stringRedisTemplate.opsForZSet();
//        this.saveValue = stringRedisTemplate.opsForHash();
    }

    public ValueOperations<String, String> getValueOperations() {
        return stringRedisTemplate.opsForValue();
    }

    /**
     * 保存set-key缓存
     * @param setKey : set的key
     * @param setHashKey ：将hash的所有的key当做值保存在set当中，为了分页查询hash使用
     * @param score : set的排序使用越小越靠前
     * @param hashKey : hash的key
     * @param hashValue : 将要保存的实际值保存到hash中
     * @return
     */
    public Boolean add(String setKey, String setHashKey, double score,String hashKey,String hashValue) {
        //保存实际value
        saveValue.put(hashKey,setHashKey,hashValue);
        //保存key
        return saveKey.add(setKey,setHashKey,score);

//            saveKey.add("patkey",patBean.getJzbbxh()+":" + patBean.getCombinationItemName(),score);
//            //保存实际value
//            saveValue.put("patinfo",patBean.getJzbbxh()+":" + patBean.getCombinationItemName(),bean);
    }

    /**
     * 获取单行数据
     * @param hashKey
     * @param setHashKey
     * @return
     */
    public String getHashValue(String hashKey,String setHashKey) {
        return saveValue.get(hashKey, setHashKey);
    }

    /**
     * 删除
     * @param hashKey
     * @param setHashKeys
     * @return
     */
    public Long deleteSetHash(String setKey,String hashKey,Object... setHashKeys) {
        //删除set-key
        saveKey.remove(setKey,setHashKeys);
        //删除hash实际值
        return saveValue.delete(hashKey, setHashKeys);
    }

    public Long size(String setKey) {
        return saveKey.size(setKey);
    }

    /**
     * 分页查询
     * @param setKey : set的key
     * @param hashKey : hash对应实际的值的key
     * @param start : 开始条目
     * @param end ：结束条目
     * @return
     */
    public List<Object> range(String setKey, String hashKey, long start, long end,Class clazz) throws IOException {
        List<Object> list = new ArrayList<>();
        Set<String> set = saveKey.range(setKey, start, end);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String setHashKey = iterator.next();
            String hashValue = this.getHashValue(hashKey, setHashKey);
            Object o = objectMapper.readValue(hashValue, clazz);
            list.add(o);
        }
        return list;
    }
}
