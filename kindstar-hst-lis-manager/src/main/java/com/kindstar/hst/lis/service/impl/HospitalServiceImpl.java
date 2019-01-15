package com.kindstar.hst.lis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.mapper.HospitalMapper;
import com.kindstar.hst.lis.pojo.AreaInfo;
import com.kindstar.hst.lis.pojo.HospitalInfo;
import com.kindstar.hst.lis.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医院查询业务类
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ZSetOperations<String,String> saveZset;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public KindStartResult findHospitalAll() throws Exception{
        List<HospitalInfo> hospitalAll = hospitalMapper.findHospitalAll();

        //将数据存进redis中
        saveZset = stringRedisTemplate.opsForZSet();
        for (HospitalInfo hospitalInfo : hospitalAll) {
            String s = objectMapper.writeValueAsString(hospitalInfo);
            //存进redis的set中
            saveZset.add("hospitalZset",s,1);
        }
        return KindStartResult.build(200, "查询成功！！！", hospitalAll);
    }
}
