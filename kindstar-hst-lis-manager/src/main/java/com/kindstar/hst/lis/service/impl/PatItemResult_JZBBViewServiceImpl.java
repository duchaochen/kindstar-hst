package com.kindstar.hst.lis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.mapper.PatItemResult_JZBBViewMapper;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;
import com.kindstar.hst.lis.service.PatItemResult_JZBBViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatItemResult_JZBBViewServiceImpl implements PatItemResult_JZBBViewService {

    @Autowired
    private PatItemResult_JZBBViewMapper patItemResult_jzbbViewMapper;

    /**
     * 查询数据
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<PatItemResult_JZBBView> selectAll(SubmitDataVo submitDataVo) throws Exception {
        PageHelper.startPage(submitDataVo.getPage(),
                submitDataVo.getLimit());
        List<PatItemResult_JZBBView> patList = patItemResult_jzbbViewMapper.selectAll(submitDataVo);
        PageInfo<PatItemResult_JZBBView> info = new PageInfo<>(patList);

//        for(PatItemResult_JZBBView patinfo : info.getList()){
//            System.out.println(patinfo);
//        }
        return info;
    }
}
