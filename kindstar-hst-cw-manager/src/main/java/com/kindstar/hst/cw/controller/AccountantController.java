package com.kindstar.hst.cw.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.cw.service.AccountantService;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountantController {

    @Autowired
    private AccountantService accountantService;

    @PostMapping("/accountant/findAccountantInfoAll")
    public PageInfo<AccountantInfo> findAccountantInfoAll(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        return accountantService.findAccountantInfoAll(submitDataVo);
    }
    /**
     * 保存财务新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @PostMapping("/accountant/insertAccountantAll")
    public KindStartResult insertAccountantAll(@RequestBody SaveCloudVo saveCloudVo) throws Exception {
        int rows = accountantService.saveAccountantAll(saveCloudVo);
        if (rows > 0) {
            return KindStartResult.build(200,"保存成功!!!");
        }
        return KindStartResult.build(500,"保存失败!!!");
    }
    /**
     * 根据条件修改审核状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/accountant/updateAuditStatus")
    public KindStartResult updateAuditStatus(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        int rows = accountantService.updateAuditStatus(submitDataVo);
        return KindStartResult.build(200,"提交成功!!!",rows);
    }
    /**
     * 取消审核
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/accountant/backDataStatus")
    public KindStartResult backDataStatus(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        int rows = accountantService.backDataStatus(submitDataVo);
        return KindStartResult.build(200,"提交成功!!!",rows);
    }

    /**
     * 取消审核
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/accountant/backOfficeData")
    public KindStartResult backOfficeData(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        int rows = accountantService.backOfficeData(submitDataVo);
        return KindStartResult.build(200,"提交成功!!!",rows);
    }
}
