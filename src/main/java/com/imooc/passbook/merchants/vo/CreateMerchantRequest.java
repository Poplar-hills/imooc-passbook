package com.imooc.passbook.merchants.vo;

import com.imooc.passbook.merchants.constants.ErrorCode;
import com.imooc.passbook.merchants.dao.MerchantDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantRequest {

    private String name;

    private String logoUrl;

    private String businessLicenseUrl;

    private String phone;

    private String address;

    // 验证请求的有效性
    public ErrorCode validate(MerchantDao merchantDao) {
        if (name == null)
            return ErrorCode.EMPTY_NAME;

        if (merchantDao.findByName(name) != null)
            return ErrorCode.DUPLICATE_NAME;

        if (logoUrl == null)
            return ErrorCode.EMPTY_LOGO;

        if (businessLicenseUrl == null)
            return ErrorCode.EMPTY_BUSINESS_LICENSE;

        if (address == null)
            return ErrorCode.EMPTY_ADDRESS;

        if (phone == null)
            return ErrorCode.EMPTY_PHONE;

        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        if (!m.matches())
            return ErrorCode.ERROR_PHONE;
    }
}
