package com.kindstar.hst.lis.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class KindStartPageInfo implements Serializable {

    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 每页数据量
     */
    private Integer limit;

    private Object where;
}
