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

    // 验证请求中字段的有效性
    public ErrorCode validate(MerchantDao merchantDao) {
        return merchantDao.findByName(name).isPresent()
            ? ErrorCode.DUPLICATE_NAME
            : ErrorCode.SUCCESS;
    }

    // 将商户请求对象转化成为商户实体对象
    public Merchant toMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setLogoUrl(logoUrl);
        merchant.setBusinessLicenseUrl(businessLicenseUrl);
        merchant.setPhone(phone);
        merchant.setAddress(address);
        return merchant;
    }
}
