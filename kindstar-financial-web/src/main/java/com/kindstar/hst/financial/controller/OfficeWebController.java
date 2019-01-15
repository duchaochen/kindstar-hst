package com.kindstar.hst.financial.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OfficeWebController {
    /**
     * 财务微服务
     */
    @Value("${global.HTTP_CW_URL}")
    public String HTTP_CW_URL;
    /**
     * 微服务调用
     */
    @Autowired
    public RestTemplate restTemplate;
    /**
     * 根据条件修改审核状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @GetMapping("/office/updateOfficeSubmit")
    public KindStartResult updateOfficeSubmit(SubmitDataVo submitDataVo) {
        try {
            return restTemplate.postForObject(HTTP_CW_URL + "/office/updateOfficeSubmit", submitDataVo, KindStartResult.class);
        } catch (Exception e) {
            return KindStartResult.build(500,"提交失败!!!");
        }
    }
}
