package com.kindstar.hst.cw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseInfo implements Serializable {

    private String jzbbxh;
    private String areaName;
    private String areaName1;
    private String sellUser;
    private String noteTime;
    private String hospitalizationName;
    private String customLevelCode;
    private String signing;
    private String realName;
    private String gender;
    private String age;
    private String combinationItemName;
    private String price;
    private String squadName;
    private String productLine;
    private String sampleType;
    private String departmentName;
    private String billDoctor;
    private String hospitalization;
    private String logisticsUser;
    private String sampleStatus;
    private String acceptanceNO;
    private String barCode;
    private String discount;

}
