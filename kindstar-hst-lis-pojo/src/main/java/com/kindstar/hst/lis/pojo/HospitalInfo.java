package com.kindstar.hst.lis.pojo;

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
public class HospitalInfo  implements Serializable {
    private String hospitalCode;
    private String areaCode;
    private String hospitalName;
    private BigDecimal discount;
    private String myBarcode;
}
