package com.imooc.passbook.merchantplatform.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, ""),
    DUPLICATE_NAME(1, "商户命名已存在"),
    MERCHANT_NOT_EXIST(2, "商户不存在");

    private Integer code;

    private String desc;
}
