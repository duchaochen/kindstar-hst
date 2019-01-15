package com.kindstar.hst.financial.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.SaleInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.common.pojo.WebLayUiPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class SaleWebController{
    /**
     * 财务微服务
     */
    @Value("${global.HTTP_CW_URL}")
    public String HTTP_CW_URL;
    /**
     * 微服务调用
     */
    @Autowired
    public RestTemplate restTemplate;

    /**
     * 查询销售表中数据
     * @param submitDataVo
     * @return
     */
    @GetMapping("/sale/searchSaleinfoData")
    public WebLayUiPageInfo searchWebSaleinfoData(SubmitDataVo submitDataVo) {
        WebLayUiPageInfo webLayUiPageInfo = new WebLayUiPageInfo();
        try {
        PageInfo pageInfo = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/findSaleinfoAll", submitDataVo, PageInfo.class);
        webLayUiPageInfo.setCode(0).setMsg("获取成功").
                setData(pageInfo.getList()).setCount(pageInfo.getTotal());
        }catch (Exception e){
            webLayUiPageInfo.setCode(0).setMsg("获取失败").
                    setData(null).setCount(0L);
        }
        return webLayUiPageInfo;
    }
    /**
     * 根据id删除销售表数据
     * @param saleid
     * @return
     * @throws Exception
     */
    @GetMapping("/sale/deleteSaleinfo")
    public Boolean deleteWebSaleinfo(String saleid) throws Exception {
        Boolean aBoolean ;
        try {
            aBoolean = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/deleteSaleinfo", saleid, Boolean.class);
        }catch (Exception e){
            aBoolean = false;
        }
        return aBoolean;
    }
    /**
     * 保存销售数据
     * @param saleInfos
     * @return
     */
    @PostMapping("/sale/saveSale")
    public KindStartResult saveWebAccountant(@RequestBody List<SaleInfo> saleInfos) {
        KindStartResult kindStartResult;
        SaveCloudVo saveCloudVo = new SaveCloudVo();
        saveCloudVo.setList(saleInfos);
        try {
            kindStartResult = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/insertAll", saveCloudVo, KindStartResult.class);
        }
        catch (Exception e){
            kindStartResult = KindStartResult.build(500,"保存失败！！");
        }
        return kindStartResult;
    }
    /**
     * 根据id修改，如果id可以从前端传过来这个方法就可以直接使用了
     * 提交销售数据到财务(暂时没有使用),
     * @param saleInfos
     * @return
     * @throws Exception
     */
    @PostMapping("/sale/updateSubmitValue")
    public KindStartResult updateWebSubmitValue(@RequestBody List<SaleInfo> saleInfos) {
        SaveCloudVo saveCloudVo = new SaveCloudVo();
        saveCloudVo.setIsSubmit(1);
        saveCloudVo.setList(saleInfos);
        KindStartResult kindStartResult;
        try {
            kindStartResult = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/updateSaveCloudVo", saveCloudVo, KindStartResult.class);
        }
        catch (Exception e){
            kindStartResult = KindStartResult.build(500,"提交失败！！");
        }
        return kindStartResult;
    }
    /**
     * 根据时间和其它条件修改销售数据提交到财务isSubmit状态
     * @param submitDataVo
     * @return
     * @throws Exception
     */
    @GetMapping("/sale/updateSubmitValueWhere")
    public KindStartResult updateWebSubmitValueWhere(SubmitDataVo submitDataVo) {
        KindStartResult kindStartResult;
        try {
            kindStartResult = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/updateSubmitValueWhere", submitDataVo, KindStartResult.class);
        }
        catch (Exception e){
            kindStartResult = KindStartResult.build(500,"提交失败！！");
        }
        return kindStartResult;
    }
}
