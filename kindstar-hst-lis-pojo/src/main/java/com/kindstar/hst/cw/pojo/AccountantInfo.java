package com.kindstar.hst.cw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AccountantInfo extends SaleInfo implements Serializable {
    private String salesNet;
    private String salesRemarks;
    private String returnedNumber;
    private String returnedTime;
    private String returnedPrice;
    private String balancePrice;
    private String countPrice;
    private String invoice;
    private String invoicePrice;
    private String invoiceNumber;
    private String rate1;
    private String rate2;
    private String payTime;
    private String payPrice;
    private String refundTime;
    private String refundPrice;
    private String receipt;
    private String idNumber;
    private String subbarCode;
    private String adjustmentTime;
    private String adjustmentDiscount;
    private String adjustedNet;
    private String adjustedDifference;
    private String adjustmentRemarks;
    private String remarks;
    private String validationInvoiceAmount;
    /**
     * 是否审核，审核将不能再次修改
     */
    private Integer isAudit;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 办事处是否提交
     */
    private Integer officeSubmit;
    /**
     * 办事处提交时间
     */
    private String officeSubmitTime;
}
