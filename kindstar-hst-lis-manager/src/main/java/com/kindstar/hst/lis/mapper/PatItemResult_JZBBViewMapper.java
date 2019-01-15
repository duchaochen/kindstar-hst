package com.kindstar.hst.lis.mapper;

import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;
import java.util.List;

public interface PatItemResult_JZBBViewMapper {
    /**
     * 查询数据
     * @return
     * @throws Exception
     */
    List<PatItemResult_JZBBView> selectAll(SubmitDataVo submitDataVo) throws Exception;
}
