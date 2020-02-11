package com.imooc.passbook.merchantplatform.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, ""),
    METHOD_ARGUMENT_NOT_VALID(1, "请求参数错误"),
    DUPLICATE_NAME(2, "商户命名已存在"),
    MERCHANT_NOT_EXIST(3, "商户不存在"),
    UNKNOWN_ERROR(4, "未知错误");

    private Integer code;

    private String desc;

    public void setMessage(String message) {  // ∵ @Setter 只能用于 class 或 field type ∴ 这里要添加方法
        this.desc = message;
    }
}
