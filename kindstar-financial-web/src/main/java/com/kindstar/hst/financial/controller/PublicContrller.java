package com.kindstar.hst.financial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.pojo.AreaInfo;
import com.kindstar.hst.lis.pojo.HospitalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基本操作controller
 */
@RestController
public class PublicContrller {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ZSetOperations<String, String> stringStringZSetOperations;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 从redis中获取医院数据
     * @return
     * @throws IOException
     */
    @GetMapping("/base/hospital")
    public KindStartResult pageLoadWebHospital() throws IOException {
        List<HospitalInfo> hospitalInfoList = new ArrayList<>();
        try {
            stringStringZSetOperations = stringRedisTemplate.opsForZSet();
            Set<String> cityZset = stringStringZSetOperations.range("hospitalZset", 0, -1);
            for (String str : cityZset) {
                HospitalInfo hospitalInfo = objectMapper.readValue(str, HospitalInfo.class);
                hospitalInfoList.add(hospitalInfo);
            }
        }catch (Exception e){
            return KindStartResult.build(500,"保存失败！！",hospitalInfoList);
        }
        return KindStartResult.build(200,"查询成功",hospitalInfoList);
    }

    /**
     * 获取地区
     * @return
     */
    @GetMapping("/base/area")
    public KindStartResult pageLoadWebArea() throws IOException {
        stringStringZSetOperations = stringRedisTemplate.opsForZSet();
        KindStartResult kindStartResult = KindStartResult.build(200,"查询成功!!");
        try {
            //加载省份
            List<AreaInfo> areaList = getAreaInfoList("areaInfoZset");
            //加载城市
            List<AreaInfo> cityList = getAreaInfoList("cityZset");
            kindStartResult.addData("areaList",areaList).addData("cityList",cityList);
        }catch (Exception e){
            return KindStartResult.build(500,"查询失败！！");
        }
        return kindStartResult;
    }
    /**
     *
     * @param key
     * @return
     * @throws IOException
     */
    private List<AreaInfo> getAreaInfoList(String key) throws IOException {
        List<AreaInfo> list = new ArrayList<>();
        Set<String> cityZset = stringStringZSetOperations.range(key, 0, -1);
        for (String str : cityZset) {
            AreaInfo areaInfo = objectMapper.readValue(str, AreaInfo.class);
            list.add(areaInfo);
        }
        return list;
    }
}
