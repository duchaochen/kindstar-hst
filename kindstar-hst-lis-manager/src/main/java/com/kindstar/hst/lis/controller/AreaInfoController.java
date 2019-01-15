package com.kindstar.hst.lis.controller;

import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.service.AreaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaInfoController {

    @Autowired
    private AreaInfoService areaInfoService;

    @GetMapping("/lis/arealist")
    public KindStartResult getAreaAll() {

        try {
            return areaInfoService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return KindStartResult.build(500,"获取失败");
    }
}
