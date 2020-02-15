package com.imooc.passbook.customerplatform.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackType {

    PASS(1, "优惠券评论"),
    APP(2, "应用评论");

    private Integer code;

    private String desc;
}
