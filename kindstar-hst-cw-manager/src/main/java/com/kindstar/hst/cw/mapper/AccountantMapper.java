package com.kindstar.hst.cw.mapper;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;

import java.util.List;

public interface AccountantMapper {
    /**
     * 查询财务表数据
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    List<AccountantInfo> findAccountantInfoAll(SubmitDataVo submitDataVo) throws Exception;
    /**
     * 保存财务数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    int saveAccountantAll(SaveCloudVo saveCloudVo) throws Exception;
    /**
     * 销售提交时使用到
     * @param submitDataVo
     * @return
     */
    int insertAccountantAll(SubmitDataVo submitDataVo);
    /**
     * 更新审核状态
     * @param submitDataVo
     * @return
     */
    int updateAuditStatus(SubmitDataVo submitDataVo);
    /**
     * 数据撤回时需要删除财务数据
     * @param submitDataVo
     * @return
     */
    int deleteAccountantAll(SubmitDataVo submitDataVo);
    /**
     * 办事处数据撤回
     * @param submitDataVo
     * @return
     */
    int backOfficeData(SubmitDataVo submitDataVo);
}
