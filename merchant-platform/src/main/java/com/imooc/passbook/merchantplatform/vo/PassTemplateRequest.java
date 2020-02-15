package com.imooc.passbook.merchantplatform.vo;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 将优惠券信息进行封装成 Value Object，用于在业务和服务之间传递
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassTemplateRequest {

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

  public static void main(String[] args) {
    System.out.println(LocalDateTime.now());
  }

  public ErrorCode validate(MerchantDao merchantDao) {
    return merchantDao.findById(id).isPresent()  // 通过 MerchantDao 去 DB 中对 id 进行有效性校验
        ? ErrorCode.SUCCESS
        : ErrorCode.MERCHANT_NOT_EXIST;
  }
}
