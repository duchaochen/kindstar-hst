package com.kindstar.hst.financial.common;

import com.kindstar.hst.excel.pojo.ExcelHeand;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ExcelCommon {
    /**
     * 创建excel操作对象
     */
    private HSSFWorkbook hwk;
    /**
     * excel的头部列表集合
     */
    private List<ExcelHeand> heands;
    /**
     * excel的sheet
     */
    private HSSFSheet sheet;
    /**
     * 要导出的对象
     */
    private List list;
    /**
     * 返回前端对象
     */
    private HttpServletResponse response;
    /**
     * excel名称
     */
    private String excelName;
    /**
     * 构造方法
     */
    public ExcelCommon() {
        this.hwk = new HSSFWorkbook();
        //创建sheet
        this.sheet = hwk.createSheet();
    }
    /**
     * 导出excel的参数
     * @param response
     * @param list
     * @param heands
     * @param excelName
     */
    public void export(HttpServletResponse response, List list, List<ExcelHeand> heands,String excelName) throws Exception {
        this.response = response;
        this.list = list;
        this.heands = heands;
        this.excelName = excelName;
        if (this.heands == null) {
            throw new Exception("heands参数值为空!");
        }
        if (this.response == null) {
            throw new Exception("response参数值为空!");
        }
        if (this.list == null || this.list.size() == 0) {
            throw new Exception("list参数值为空!");
        }
        this.export();
    }

    /**
     * 导出财务Excel数据
     */
    private void export() {

        //生成头部
        this.createSheetHeand();

        OutputStream os = null;
        try {
            //生成数据条目
            this.createBody();
            this.response.setContentType("application/vnd.ms-excel;charset=utf-8");

            os = response.getOutputStream();
            //默认Excel名称
            this.response.setHeader("Content-disposition", "attachment;filename="+this.excelName);
            this.hwk.write(os);
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException("bean转换错误");
        }
    }

    /**
     * 创建头部
     */
    private void createSheetHeand() {
        //创建第一行
        HSSFRow row = this.sheet.createRow(0);
        //设置高度
        row.setHeight((short) (16 * 20));
        for (int i = 0; i < this.heands.size(); i++) {
            ExcelHeand heand = this.heands.get(i);
            //设置行的数据
            row.createCell(i).setCellValue(heand.getSheetCName());
            //设置单元格宽度，设置宽度的时候是一个字符为550个宽度,这里就需要字符串的长度乘以550
            this.sheet.setColumnWidth(i,heand.getColumnWidth());
        }
    }

    /**
     * 生成条目
     * @param <T>
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private void createBody() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (int i = 0; i < this.list.size(); i++) {
            //创建行(需要从第二行开始写，因为第一行是列头)
            HSSFRow row = this.sheet.createRow(i+1);
            //设置高度
            row.setHeight((short) (16 * 20));
            this.createColumn(this.list.get(i),row);
        }
    }

    /**
     * 创建列
     * @param object
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private void createColumn(Object object,HSSFRow row) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (int i = 0; i < this.heands.size(); i++) {
            ExcelHeand field = this.heands.get(i);
            //根据每个字段名称获取对应的值
            Object propertyValue = PropertyUtils.getProperty(object, field.getSheetEName());
            String value;
            if (propertyValue instanceof Date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                value = format.format(propertyValue);
            }else{
                value = String.valueOf(propertyValue);
            }
            //设置行的数据
            row.createCell(i).setCellValue(value=="null"?"":value);
            //设置单元格宽度,设置宽度的时候是一个字符为650个宽度,这里就需要字符串的长度乘以256
            this.sheet.setColumnWidth(i,field.getColumnWidth());
        }
    }



}
