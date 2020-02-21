package com.imooc.passbook.customerplatform.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "SUCCESS"),
    UNKNOWN_ERROR(1, "未知错误");

    private Integer code;

    private String desc;

    public void setMessage(String message) {  // ∵ @Setter 只能用于 class 或 field type ∴ 这里要添加方法
        this.desc = message;
    }
}
