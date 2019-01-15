package com.kindstar.hst.lis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @AllArgsConstructor：全参的构造方法
 * @NoArgsConstructor：无参的构造方法
 * @Data:get和set方法
 * @Accessors(chain = true)使用链式编程
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PatItemResult_JZBBView implements Serializable {

    private String jzbbxh;
    private String areaName;
    private String areaName1;
    private String sellUser;
    private String departmentName;
    private String realName;
    private String age;
    private String gender;
    private String hospitalization;
    private String barCode;
    private String customLevelCode;
    private String hospitalizationName;
    private String signing;
    private String billDoctor;
    private String combinationItemName;
    private String noteTime;
    private String squadName;
    private String productLine;
    private String sampleType;
    private String acceptanceNO;
    private String price;
    private String logisticsUser;
    private String sampleStatus;
    private String discount;

//    private String outSetName;
//    private String fenDanUserName;
//    private String testType;
//    private String shr;
//    private String auditTime;
//    private String medicareNo;
//    private String bedNum;
//    private String gatherTime;
//    private String assemblyID;
//    private String isImg;
//    private String iDCard;
//    private String applicationAddress;
//    private String gather;
//    private String assemblyName;
//    private String fenDanTime;
//    private String qcljssj;
//    private String kSDBarCode;
//    private String noteUser;
//    private String qcljsry;
//    private String reportTime;
//    private String returned;
//    private String invoice;

}
