package com.kindstar.hst.lis.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SubmitDataVo extends KindStartPageInfo {
    /**
     * 地区
     */
    private String areaval;
    /**
     * 医院
     */
    private String hospitalval;
    /**
     * 开始时间
     */
    private String startdate;
    /**
     * 结束时间
     */
    private String stopdate;
    /**
     * 提交状态
     */
    private Integer isSubmit;
    /**
     * 提交的修改状态
     */
    private Integer editIsSubmit;
    /**
     * 是否审核
     */
    private Integer isAudit;
    /**
     * 修改的审核状态
     */
    private Integer editIsAudit;
    /**
     * 是否办事处提交
     */
    private Integer officeSubmit;
    /**
     * 修改的办事处提交状态
     */
    private Integer editOfficeSubmit;

}
