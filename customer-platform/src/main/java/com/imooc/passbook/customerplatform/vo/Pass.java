package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户领取的优惠券的对象定义
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pass {

    private Long userId;  // 用户 id

    private String rowKey;  // pass 在 HBase 中的 RowKey

    private String templateId;  // PassTemplate 在 HBase 中的 RowKey（相当于外键）

    private String token;  // 优惠券 token（若优惠券没有，则填充"-1"）

    private LocalDateTime collectDate;  // 领取日期

    private LocalDateTime consumeDate;  // 消费日期（不为空就代表已经被消费了）
}
