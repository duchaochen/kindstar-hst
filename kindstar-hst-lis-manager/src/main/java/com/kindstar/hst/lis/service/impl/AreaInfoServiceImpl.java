package com.kindstar.hst.lis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.config.SpringContextUtil;
import com.kindstar.hst.lis.mapper.AreaInfoMapper;
import com.kindstar.hst.lis.pojo.AreaInfo;
import com.kindstar.hst.lis.service.AreaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取所有地区
 */
@Service
public class AreaInfoServiceImpl implements AreaInfoService {

    @Autowired
    private AreaInfoMapper areaInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ZSetOperations<String,String> saveZset;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询地区与城市
     * @return
     * @throws Exception
     */
    @Override
    public KindStartResult findAll() throws Exception{

        List<AreaInfo> all = areaInfoMapper.findAll();
        List<AreaInfo> areaList = all.stream().filter(areaInfo -> "0".equals(areaInfo.getParentAreaCode())).collect(Collectors.toList());
        List<AreaInfo> cityList = all.stream().filter(areaInfo -> !"0".equals(areaInfo.getParentAreaCode())).collect(Collectors.toList());
        //将数据存进redis中
        saveZset = stringRedisTemplate.opsForZSet();
        for (AreaInfo areaInfo : areaList) {
            String s = objectMapper.writeValueAsString(areaInfo);
            //存进redis的set中
            saveZset.add("areaInfoZset",s,1);
        }
        //将数据存进redis中
        for (AreaInfo city : cityList) {
            String s = objectMapper.writeValueAsString(city);
            //存进redis的set中
            saveZset.add("cityZset",s,1);
        }

        return KindStartResult.build(200,"保存到redis中成功");
    }
}
