package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 商户投放的优惠券的对象定义
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassTemplate {

    @NotNull(message = "商户 id 为空")
    private Integer id;

    @NotBlank(message = "优惠券标题为空")
    private String title;

    @NotBlank(message = "优惠券摘要为空")
    private String summary;

    @NotBlank(message = "优惠券详细信息为空")
    private String desc;

    @NotNull(message = "优惠券最大发放个数为空")
    private Long limit;

    @NotNull(message = "优惠券是否有 token 需要商户核销为空")
    private Boolean hasToken;

    @NotNull(message = "优惠券背景色为空")
    private Integer background;

    @NotNull(message = "优惠券开始时间为空")
    private LocalDateTime startTime;

    @NotNull(message = "优惠券结束时间为空")
    private LocalDateTime endTime;
}
