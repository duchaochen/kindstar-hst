package com.kindstar.hst.cw.service.impl;

import com.kindstar.hst.cw.mapper.OfficeMapper;
import com.kindstar.hst.cw.service.OfficeService;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeServiceImpl implements OfficeService {

    @Autowired
    private OfficeMapper officeMapper;
    /**
     * 更新办事处是否提交
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @Override
    public int updateofficeSubmit(SubmitDataVo submitDataVo) throws Exception {
        int rows = officeMapper.updateofficeSubmit(submitDataVo);
        return rows;
    }
}
