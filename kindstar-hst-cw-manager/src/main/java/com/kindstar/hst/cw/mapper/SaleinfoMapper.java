package com.kindstar.hst.cw.mapper;

import com.kindstar.hst.cw.pojo.SaleInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;

import java.util.List;
import java.util.Map;

public interface SaleinfoMapper {
    /**
     * 同步数据所有到销售表
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    boolean insertLisInfoAll(SaveCloudVo saveCloudVo) throws Exception;
    /**
     * 查询销售表
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    List<SaleInfo> findSaleinfoAll(SubmitDataVo submitDataVo) throws Exception;
    /**
     * 删除销售表数据
     * @param saleid
     * @return
     * @throws Exception
     */
    boolean deleteSaleinfo(String saleid) throws Exception;
    /**
     * 保存销售新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    int updateSaleInfoData(SaveCloudVo saveCloudVo) throws Exception;
    /**
     * 根据id修改
     * 提交销售数据到财务(暂时没有使用)
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    int updateSaveCloudVo(SaveCloudVo saveCloudVo) throws Exception;
    /**
     * 根据时间和其它条件修改销售数据提交到财务isSubmit状态
     * @param map
     * @return
     * @throws Exception
     */
    int updateSubmitValueWhere(SubmitDataVo map) throws Exception;
}
