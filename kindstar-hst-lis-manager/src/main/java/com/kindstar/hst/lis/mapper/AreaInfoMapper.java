package com.kindstar.hst.lis.mapper;

import com.kindstar.hst.lis.pojo.AreaInfo;

import java.util.List;

/**
 * 获取所有地区
 */
public interface AreaInfoMapper {

    List<AreaInfo> findAll() throws Exception;
}
