package com.kindstar.hst.lis.service;

import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.pojo.HospitalInfo;

import java.util.List;

public interface HospitalService {

    KindStartResult findHospitalAll() throws Exception;
}
