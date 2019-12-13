package com.imooc.passbook.merchants.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠券背景颜色枚举
 */

@AllArgsConstructor
@Getter
public enum TemplateColor {

    RED(1, "红色"),
    GREEN(2, "绿色"),
    BLUE(3, "蓝色");

    // 颜色代码
    private Integer code;

    // 颜色描述
    private String color;
}
