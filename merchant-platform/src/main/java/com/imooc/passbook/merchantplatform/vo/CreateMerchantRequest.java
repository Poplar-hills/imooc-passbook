package com.imooc.passbook.merchantplatform.vo;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import com.imooc.passbook.merchantplatform.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMerchantRequest {

  @NotBlank(message = "商户命名为空")
  private String name;

  @NotBlank(message = "商户 logo 为空")
  private String logoUrl;

  @NotBlank(message = "商户营业执照为空")
  private String businessLicenseUrl;

  @Pattern(
      regexp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[8,9]))\\d{8}$",
      message = "商户联系电话无效")
  @NotBlank(message = "商户联系电话为空")
  private String phone;

  @NotBlank(message = "商户地址为空")
  private String address;

  public ErrorCode validate(MerchantDao merchantDao) {  // TODO: [question] 需要特地去 DB 中交易一次？为何不直接 save？
    return merchantDao.findByName(name).isPresent()  // 通过 MerchantDao 去 DB 中对 name 进行有效性校验
        ? ErrorCode.DUPLICATE_NAME
        : ErrorCode.SUCCESS;
  }
}
