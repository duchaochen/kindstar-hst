package com.kindstar.hst.cw.service;

import com.kindstar.hst.lis.common.pojo.SubmitDataVo;

public interface OfficeService {
    /**
     * 更新办事处是否提交
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    int updateofficeSubmit(SubmitDataVo submitDataVo) throws Exception;
}
