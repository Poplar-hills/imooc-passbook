package com.imooc.passbook.merchants.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, ""),    // TODO: ErrorCode 中不应该有 SUCCESS
    EMPTY_NAME(1, "商户命名为空"),
    DUPLICATE_NAME(2, "商户命名已存在"),
    EMPTY_LOGO(3, "商户 logo 为空"),
    EMPTY_BUSINESS_LICENSE(4, "商户营业执照为空"),
    EMPTY_PHONE(5, "商户联系电话为空"),
    ERROR_PHONE(6, "商户联系电话无效"),
    EMPTY_ADDRESS(7, "商户地址悟空"),
    MERCHANT_NOT_EXIST(8, "商户不存在");

    private Integer code;

    private String desc;
}
