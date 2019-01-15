package com.kindstar.hst.lis.mapper;

import com.kindstar.hst.lis.pojo.HospitalInfo;

import java.util.List;

public interface HospitalMapper {

    List<HospitalInfo> findHospitalAll() throws Exception;
}
