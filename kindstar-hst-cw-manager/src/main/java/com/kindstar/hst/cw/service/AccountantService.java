package com.kindstar.hst.cw.service;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;

public interface AccountantService {
    /**
     * 查询财务表数据
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    PageInfo<AccountantInfo> findAccountantInfoAll(SubmitDataVo submitDataVo) throws Exception;
    /**
     * 保存财务新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    int saveAccountantAll(SaveCloudVo saveCloudVo) throws Exception;
    /**
     * 根据条件修改审核状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    int updateAuditStatus(SubmitDataVo submitDataVo);
    /**
     * 销售数据撤回
     * @param submitDataVo
     * @return
     */
    int backDataStatus(SubmitDataVo submitDataVo) throws Exception;
    /**
     * 办事处数据撤回
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    int backOfficeData(SubmitDataVo submitDataVo)throws Exception;
}
