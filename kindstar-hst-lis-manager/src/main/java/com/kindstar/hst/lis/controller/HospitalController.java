package com.kindstar.hst.lis.controller;

import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/lis/hospital")
    public KindStartResult findHospitalAll() {

        try {
            return (KindStartResult) hospitalService.findHospitalAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return KindStartResult.build(500,"查询错误");
    }

}
