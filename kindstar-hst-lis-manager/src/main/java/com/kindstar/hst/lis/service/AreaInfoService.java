package com.kindstar.hst.lis.service;

import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.pojo.AreaInfo;

import java.util.List;

/**
 * 获取所有地区
 */
public interface AreaInfoService {

    KindStartResult findAll() throws Exception;
}
