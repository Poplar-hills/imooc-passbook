package com.imooc.passbook.customerplatform.vo;

import com.imooc.passbook.customerplatform.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfo {

  private Pass pass;  // 优惠券

  private PassTemplate passTemplate;  // 优惠券模板

  private Merchant merchant;  // 优惠券对应的商户
}
