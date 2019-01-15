package com.kindstar.hst.cw.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.mapper.AccountantMapper;
import com.kindstar.hst.cw.mapper.SaleinfoMapper;
import com.kindstar.hst.cw.pojo.SaleInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.cw.service.SaleinfoService;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拉取lis系统数据插入到base表的业务类
 */
@Service
public class SaleinfoServiceImpl implements SaleinfoService {

    @Autowired
    private SaleinfoMapper tempLisMapper;

    @Autowired
    private AccountantMapper accountantMapper;
    /**
     * 批量添加Base表数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @Override
    public boolean insertLisInfoAll(SaveCloudVo saveCloudVo) {
        try {
            return tempLisMapper.insertLisInfoAll(saveCloudVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 查询销售表
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<SaleInfo> findSaleinfoAll(SubmitDataVo submitDataVo) throws Exception {
        PageHelper.startPage(submitDataVo.getPage(),submitDataVo.getLimit());
        List<SaleInfo> saleinfoAll = tempLisMapper.findSaleinfoAll(submitDataVo);
        PageInfo<SaleInfo> pageInfo = new PageInfo<>(saleinfoAll);
        return pageInfo;
    }
    /**
     * 根据id删除销售表数据
     * @param saleid
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteSaleinfo(String saleid) throws Exception {
        return tempLisMapper.deleteSaleinfo(saleid);
    }
    /**
     * 保存销售新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @Override
    public int updateSaleInfoData(SaveCloudVo saveCloudVo) throws Exception {
        return tempLisMapper.updateSaleInfoData(saveCloudVo);
    }
    /**
     * 根据id修改，如果id可以从前端传过来这个方法就可以直接使用了
     * 提交销售数据到财务(暂时没有使用),
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @Override
    public int updateSaveCloudVo(SaveCloudVo saveCloudVo) throws Exception {
        return tempLisMapper.updateSaveCloudVo(saveCloudVo);
    }
    /**
     * 根据时间和其它条件修改销售数据提交到财务isSubmit状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @Override
    public int updateSubmitValueWhere(SubmitDataVo submitDataVo) throws Exception {
        int rows = tempLisMapper.updateSubmitValueWhere(submitDataVo);
        if (rows > 0) {
            submitDataVo.setIsSubmit(1);
            int i = accountantMapper.insertAccountantAll(submitDataVo);
            if (i == 0) {
                throw new Exception("插入财务表出错");
            }
        }
        return rows;
    }
}
