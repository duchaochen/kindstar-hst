package com.kindstar.hst.financial.controller;

import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.AccountantInfo;
import com.kindstar.hst.cw.pojo.SaleInfo;
import com.kindstar.hst.excel.pojo.ExcelHeand;
import com.kindstar.hst.financial.common.ExcelCommon;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelOperationController {
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
    @Value("${global.pageSize}")
    private int pageSize;

    private final String SALE_URL = "/saleInfo/findSaleinfoAll";
    private final String ACCOUNTANT_URL = "/accountant/findAccountantInfoAll";

    @Autowired
    private ExcelCommon excelCommon;
    /**
     * 销售导出excel
     * @param submitDataVo
     * @param response
     */
    @RequestMapping("/exportSaleData")
    public void exportSaleData(SubmitDataVo submitDataVo,HttpServletResponse response) {
        //获取数据
        List<SaleInfo> saleInfos = this.getList(submitDataVo,SALE_URL);
        if (saleInfos.size() > 0) {
            //开始导出Excel
            this.export(response,saleInfos,0,"sale.xls");
        }
    }
    /**
     * 财务数据导出excel
     * @param submitDataVo
     * @param response
     */
    @RequestMapping("/exportAccountantData")
    public void exportAccountantData(SubmitDataVo submitDataVo,HttpServletResponse response) {
        //获取数据
        List<AccountantInfo> accountantInfos =this.getList(submitDataVo,ACCOUNTANT_URL);
        if (accountantInfos.size() > 0) {
            ////开始导出Excel
            this.export(response,accountantInfos,1,"accountant.xls");
        }
    }
    /**
     * 导出
     * @param response
     * @param list
     * @param heandStatus
     * @param excelNane
     */
    private void export(HttpServletResponse response,List list,int heandStatus,String excelNane) {
        //列头集合
        List<ExcelHeand> heands = this.sheetHeand(heandStatus);
        //开始导出Excel
        try {
            excelCommon.export(response,list,heands,excelNane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取数据
     * @param submitDataVo
     * @param url
     * @param <T>
     * @return
     */
    private <T> List<T> getList(SubmitDataVo submitDataVo,String url) {
        //默认第一页
        int pageNum = 1;
        List<T> list = new ArrayList<>();
        try {
            PageInfo<T> pageInfo = this.getLisData(submitDataVo,pageNum,url);
            int pages = pageInfo.getPages();
            //如果超过一页才会开始同步
            if (pages > 0) {
                //合并list集合
                list.addAll(pageInfo.getList());
                //循环读取
                for(pageNum = 2;pageNum <= pages;pageNum++) {
                    pageInfo = this.getLisData(submitDataVo,pageNum,url);
                    //合并list集合
                    list.addAll(pageInfo.getList());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 查询要导出的销售需要的数据
     * @param pageNum
     * @return
     */
    private <T> PageInfo<T> getLisData(SubmitDataVo submitDataVo, int pageNum, String url) {
        //包装传输数据
        submitDataVo.setPage(pageNum).setLimit(pageSize);
        PageInfo pageInfo = restTemplate.postForObject(HTTP_CW_URL + url, submitDataVo, PageInfo.class);
        return pageInfo;
    }
    /**
     * 头部定义
     * @param status : 1表示要添加财务表头
     * @return
     */
    private List<ExcelHeand> sheetHeand(int status) {
        List<ExcelHeand> heands = new ArrayList<>();
        heands.add(new ExcelHeand("areaName", "省份",650*3));
        heands.add(new ExcelHeand("areaName1", "城市",650*8));
        heands.add(new ExcelHeand("sellUser", "销售人员",650*4));
        heands.add(new ExcelHeand("departmentName", "科室",650*7));
        heands.add(new ExcelHeand("realName", "病人姓名",650*4));
        heands.add(new ExcelHeand("age", "岁",650*2));
        heands.add(new ExcelHeand("gender", "性别",650*2));
        heands.add(new ExcelHeand("hospitalization", "住院号",650*8));
        heands.add(new ExcelHeand("barCode", "总条码号",650*7));
        heands.add(new ExcelHeand("customLevelCode", "医院级别",650*4));
        heands.add(new ExcelHeand("hospitalizationName", "医院名称",650*14));
        heands.add(new ExcelHeand("signing", "是否签约",650*4));
        heands.add(new ExcelHeand("billDoctor", "医生",650*4));
        heands.add(new ExcelHeand("combinationItemName", "项目名称",650*16));
        heands.add(new ExcelHeand("noteTime", "实际录入时间",650*7));
        heands.add(new ExcelHeand("squadName", "检验小组",650*4));
        heands.add(new ExcelHeand("productLine", "产品线",650*4));
        heands.add(new ExcelHeand("sampleType", "样本类型",650*6));
        heands.add(new ExcelHeand("acceptanceNO", "检验时机",650*4));
        heands.add(new ExcelHeand("logisticsUser", "物流人员",650*4));
        heands.add(new ExcelHeand("sampleStatus", "接检类型",650*4));
        heands.add(new ExcelHeand("price", "项目价格",650*4));
        heands.add(new ExcelHeand("discount", "折扣",650*2));
        heands.add(new ExcelHeand("reportTime", "出报告时间",650*7));
        heands.add(new ExcelHeand("invoiceTime", "开票日期",650*7));
        heands.add(new ExcelHeand("realPrice", "实收金额",650*4));
        heands.add(new ExcelHeand("submitTime", "提交时间",650*7));
        if (status == 1) {
            heands.add(new ExcelHeand("salesNet", "销售净额",650*7));
            heands.add(new ExcelHeand("salesRemarks", "销售备注",650*7));
            heands.add(new ExcelHeand("returnedNumber", "回款编号",650*7));
            heands.add(new ExcelHeand("returnedTime", "回款日期",650*7));
            heands.add(new ExcelHeand("returnedPrice", "回款金额",650*7));
            heands.add(new ExcelHeand("balancePrice", "应收余额",650*7));
            heands.add(new ExcelHeand("countPrice", "回款总额",650*7));
            heands.add(new ExcelHeand("salesNet", "销售净额",650*7));
            heands.add(new ExcelHeand("invoice", "开票类型",650*7));
            heands.add(new ExcelHeand("invoicePrice", "开票金额",650*7));
            heands.add(new ExcelHeand("invoiceNumber", "发票号码",650*7));
            heands.add(new ExcelHeand("rate1", "税率",650*7));
            heands.add(new ExcelHeand("rate2", "税额",650*7));
            heands.add(new ExcelHeand("payTime", "补交日期",650*7));
            heands.add(new ExcelHeand("payPrice", "补交金额",650*7));
            heands.add(new ExcelHeand("receipt", "发票",650*7));
            heands.add(new ExcelHeand("idNumber", "ID号",650*7));
            heands.add(new ExcelHeand("subbarCode", "子条码",650*7));
            heands.add(new ExcelHeand("adjustmentTime", "调整日期",650*7));
            heands.add(new ExcelHeand("adjustmentDiscount", "调整折扣",650*7));
            heands.add(new ExcelHeand("adjustedNet", "调整后净额",650*7));
            heands.add(new ExcelHeand("adjustedDifference", "调整差额",650*7));
            heands.add(new ExcelHeand("adjustmentRemarks", "调整备注",650*7));
            heands.add(new ExcelHeand("remarks", "备注",650*7));
            heands.add(new ExcelHeand("validationInvoiceAmount", "开票金额验证",650*7));
            heands.add(new ExcelHeand("isAudit", "是否审核",650*7));
            heands.add(new ExcelHeand("auditTime", "审核时间",650*7));
        }
        return heands;
    }
}
