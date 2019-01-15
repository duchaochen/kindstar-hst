package com.kindstar.hst.lis.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WebLayUiPageInfo {
    private Integer code;
    private String msg;
    private Long count;
    private Object data;
}
