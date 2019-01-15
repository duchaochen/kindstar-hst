package com.kindstar.hst.lis.service;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;

public interface PatItemResult_JZBBViewService {
    /**
     * 查询数据
     * @return
     * @throws Exception
     */
    PageInfo<PatItemResult_JZBBView> selectAll(SubmitDataVo submitDataVo) throws Exception;
}
