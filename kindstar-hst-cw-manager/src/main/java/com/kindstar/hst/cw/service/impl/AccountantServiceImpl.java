package com.kindstar.hst.cw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.mapper.AccountantMapper;
import com.kindstar.hst.cw.mapper.SaleinfoMapper;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.cw.service.AccountantService;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountantServiceImpl implements AccountantService {

    @Autowired
    private AccountantMapper accountantMapper;

    @Autowired
    private SaleinfoMapper saleinfoMapper;
    /**
     * 查询财务表数据
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<AccountantInfo> findAccountantInfoAll(SubmitDataVo submitDataVo) throws Exception {
//        "saleid asc"
        PageHelper.startPage(submitDataVo.getPage(), submitDataVo.getLimit(),"saleid asc");
        //查询数据
        List<AccountantInfo> infolist = accountantMapper.findAccountantInfoAll(submitDataVo);
        PageInfo<AccountantInfo> pageInfo = new PageInfo<>(infolist);
        return pageInfo;
    }
    /**
     * 保存财务新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @Override
    public int saveAccountantAll(SaveCloudVo saveCloudVo) throws Exception {
        int rows = accountantMapper.saveAccountantAll(saveCloudVo);
        return rows;
    }
    /**
     * 更新审核状态
     * @param submitDataVo
     * @return
     */
    @Override
    public int updateAuditStatus(SubmitDataVo submitDataVo) {
        return accountantMapper.updateAuditStatus(submitDataVo);
    }
    /**
     * 财务数据撤回
     * @param submitDataVo
     * @return
     */
    @Override
    public int backDataStatus(SubmitDataVo submitDataVo) throws Exception {
        int deleteRows = accountantMapper.deleteAccountantAll(submitDataVo);
        if (deleteRows > 0) {
            //删除成功后才能改变销售的isSubmit=0的状态
            saleinfoMapper.updateSubmitValueWhere(submitDataVo);
        }
        return 0;
    }

    /**
     * 办事处数据撤回
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @Override
    public int backOfficeData(SubmitDataVo submitDataVo) throws Exception {
        return accountantMapper.backOfficeData(submitDataVo);
    }
}
