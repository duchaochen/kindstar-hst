package com.kindstar.hst.lis.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.lis.common.pojo.KindStartPageInfo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;
import com.kindstar.hst.lis.service.PatItemResult_JZBBViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据拉取controller层
 */
@RestController
public class PatItemResultJZBBViewController {

    @Autowired
    private PatItemResult_JZBBViewService patItemResultJzbbViewService;

    @PostMapping("/lis/getPatResult")
    public PageInfo<PatItemResult_JZBBView> getLisPatItemResult(@RequestBody SubmitDataVo submitDataVo) {
        PageInfo<PatItemResult_JZBBView> pageInfo = null;
        try {
            pageInfo = patItemResultJzbbViewService.selectAll(submitDataVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  pageInfo;
    }
}
