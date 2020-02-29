package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取优惠券的请求对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectPassTemplateRequest {

    private Long userId;

    private PassTemplate passTemplate;
}
