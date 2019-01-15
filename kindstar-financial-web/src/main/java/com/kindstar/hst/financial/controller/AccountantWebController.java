package com.kindstar.hst.financial.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.common.pojo.WebLayUiPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class AccountantWebController {
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

    @GetMapping("/accountant/searchAccountantData")
    public WebLayUiPageInfo searchWebAccountantData(SubmitDataVo submitDataVo) {
        WebLayUiPageInfo webLayUiPageInfo = new WebLayUiPageInfo();
        PageInfo pageInfo = restTemplate.postForObject(HTTP_CW_URL + "/accountant/findAccountantInfoAll", submitDataVo, PageInfo.class);
        webLayUiPageInfo.setCode(0).setMsg("查询成功").
                setCount(pageInfo.getTotal()).setData(pageInfo.getList());
        return webLayUiPageInfo;
    }
    /**
     * 保存财务新增字段
     * @param accountantInfos
     * @return
     */
    @PostMapping("/accountant/saveAccountant")
    public KindStartResult saveWebAccountant(@RequestBody List<AccountantInfo> accountantInfos) {
        SaveCloudVo saveCloudVo = new SaveCloudVo();
        saveCloudVo.setList(accountantInfos);
        try {
            return restTemplate.postForObject(HTTP_CW_URL + "/accountant/insertAccountantAll", saveCloudVo, KindStartResult.class);
        }
        catch (Exception e){
            return KindStartResult.build(500,"出现异常,保存失败！！");
        }
    }
    /**
     * 根据时间和其它条件修改销售数据提交到财务isSubmit状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @GetMapping("/accountant/updateAuditStatus")
    public KindStartResult updateWebAuditStatus(SubmitDataVo submitDataVo) {
        try {
            return restTemplate.postForObject(HTTP_CW_URL + "/accountant/updateAuditStatus", submitDataVo, KindStartResult.class);
        }
        catch (Exception e){
            return KindStartResult.build(500,"审核失败！！");
        }
    }
    /**
     * 销售数据撤回
     * @param submitDataVo
     * @return
     */
    @GetMapping("/accountant/backDataStatus")
    public KindStartResult backWebDataStatus(SubmitDataVo submitDataVo) {
        try {
            return restTemplate.postForObject(HTTP_CW_URL + "/accountant/backDataStatus", submitDataVo, KindStartResult.class);
        }
        catch (Exception e){
            return KindStartResult.build(500,"审核失败！！");
        }
    }

    /**
     * 办事处数据撤回
     * @param submitDataVo
     * @return
     */
    @GetMapping("/accountant/backOfficeData")
    public KindStartResult backWebOfficeData(SubmitDataVo submitDataVo) {
        try {
            return restTemplate.postForObject(HTTP_CW_URL + "/accountant/backOfficeData", submitDataVo, KindStartResult.class);
        }
        catch (Exception e){
            return KindStartResult.build(500,"审核失败！！");
        }
    }
}
