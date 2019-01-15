package com.kindstar.hst.cw.controller;
import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.SaleInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.cw.service.SaleinfoService;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据批量插入controller层
 */
@RestController
public class SaleInfoController {

    @Autowired
    private SaleinfoService saleinfoService;

    @PostMapping("/saleInfo/insertAll")
    public KindStartResult insertAll(@RequestBody SaveCloudVo saveCloudVo) throws Exception {

        boolean lisInfoAll = saleinfoService.insertLisInfoAll(saveCloudVo);
        if (lisInfoAll) {
            return KindStartResult.build(200,"批量添加成功!!!");
        }
        return KindStartResult.build(500, "保存出错");
    }

    /**
     *
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/saleInfo/findSaleinfoAll")
    public PageInfo<SaleInfo> findSaleinfoAll(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        PageInfo<SaleInfo> saleinfoAll = saleinfoService.findSaleinfoAll(submitDataVo);
        return saleinfoAll;
    }


    /**
     * 根据id删除销售表数据
     * @param saleid
     * @return
     * @throws Exception
     */
    @PostMapping("/saleInfo/deleteSaleinfo")
    public Boolean deleteSaleinfo(@RequestBody String saleid) throws Exception {
        boolean b = saleinfoService.deleteSaleinfo(saleid);
        return b;
    }
    /**
     * 保存销售新增字段数据
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @PostMapping("/saleInfo/saveSale")
    public KindStartResult saveAccountant(@RequestBody SaveCloudVo saveCloudVo) throws Exception {
        int rows = saleinfoService.updateSaleInfoData(saveCloudVo);
        return KindStartResult.build(200,"保存成功",rows);
    }
    /**
     * 根据id修改，如果id可以从前端传过来这个方法就可以直接使用了
     * 提交销售数据到财务(暂时没有使用),
     * @param saveCloudVo
     * @return
     * @throws Exception
     */
    @PostMapping("/saleInfo/updateSaveCloudVo")
    public KindStartResult updateSaveCloudVo(@RequestBody SaveCloudVo saveCloudVo) throws Exception {
        int rows = saleinfoService.updateSaveCloudVo(saveCloudVo);
        if (rows == -1) {
            return KindStartResult.build(200,"没有查询到任何数据，提交失败!!!",rows);
        }
        return KindStartResult.build(200,"提交成功!!!",rows);
    }

    /**
     * 根据时间和其它条件修改销售数据提交到财务isSubmit状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @PostMapping("/saleInfo/updateSubmitValueWhere")
    public KindStartResult updateSubmitDataVoWhere(@RequestBody SubmitDataVo submitDataVo) throws Exception {
        int rows = saleinfoService.updateSubmitValueWhere(submitDataVo);
        return KindStartResult.build(200,"提交成功!!!",rows);
    }
}
