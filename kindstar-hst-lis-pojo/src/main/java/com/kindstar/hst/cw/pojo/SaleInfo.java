package com.kindstar.hst.cw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 销售表实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SaleInfo extends BaseInfo implements Serializable{

    private Integer saleid;
    /**
     * 报告时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date reportTime;
    /**
     * 开票时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date invoiceTime;
    /**
     * 实收金额
     */
    private Double realPrice;
    /**
     * 是否提交，提交后不能在进行修改,必须得撤回才能再次修改
     */
    private Integer isSubmit;
    /**
     * 销售提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitTime;
}
