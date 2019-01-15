package com.kindstar.hst.lis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AreaInfo implements Serializable {

    private String areaCode;
    private String areaName;
    private String superintendent;
    private String parentAreaCode;
    private String cityLevel;
    private String valid;
    private String areaNumber;
    private String wb3;
    private String py3;
    private String createTime;
}
