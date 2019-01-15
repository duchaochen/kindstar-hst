package com.kindstar.hst.cw.controller;

import com.kindstar.hst.cw.service.OfficeService;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfficeController {

    @Autowired
    private OfficeService accountantService;

    /**
     * 根据条件修改审核状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/office/updateOfficeSubmit")
    public KindStartResult updateOfficeSubmit(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        int rows = accountantService.updateofficeSubmit(submitDataVo);
        return KindStartResult.build(200,"提交成功!!!",rows);
    }
}
