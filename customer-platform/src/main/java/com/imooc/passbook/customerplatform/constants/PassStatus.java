package com.imooc.passbook.customerplatform.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassStatus {

    UNUSED(2, "未使用"),
    USED(2, "已使用"),
    ALL(3, "已领取");

    private Integer code;

    private String desc;
}
