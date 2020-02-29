package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 某个用户可领取的优惠券列表
 * - ∵ 不同用户领取过不同的优惠券 ∴ 每个用户可领取的优惠券也是不同的，这也是需要 userId 的原因。
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectablePassTemplates {

  private Long userId;

  private List<PassTemplateInfo> passTemplateInfos;
}
