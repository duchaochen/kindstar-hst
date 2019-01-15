package com.kindstar.hst.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ExcelHeand {
    /**
     * 英文名称
     */
    private String sheetEName;
    /**
     * 中文名称
     */
    private String sheetCName;
    /**
     *单元格的长度
     *设置宽度的时候是一个字符为650个宽度,这里就需要字符串的长度乘以256
     */
    private int columnWidth;
}
