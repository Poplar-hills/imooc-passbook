package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商户投放的优惠券的对象定义
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplate {

    private Integer id;  // 商户 id

    private String title;  // 标题

    private String summary;  // 摘要

    private String desc;  // 详细信息

    private Long limit;  // 最大发放个数

    private Boolean hasToken;  // 优惠券是否有 Token, 用于商户核销

    private Integer background;  // 背景色

    private LocalDateTime startTime;  // 优惠券开始时间

    private LocalDateTime endTime;  // 优惠券结束时间
}
