package com.imooc.passbook.merchants.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, ""),    // TODO: ErrorCode 中不应该有 SUCCESS
    DUPLICATE_NAME(1, "商户命名重复"),
    EMPTY_LOGO(2, "商户 logo 为空"),
    EMPTY_BUSINESS_LICENSE(3, "商户营业执照为空"),
    ERROR_PHONE(4, "商户联系电话无效"),
    EMPTY_ADDRESS(5, "商户地址悟空"),
    MERCHANT_NOT_EXIST(6, "商户不存在");

    private Integer code;

    private String desc;
}
