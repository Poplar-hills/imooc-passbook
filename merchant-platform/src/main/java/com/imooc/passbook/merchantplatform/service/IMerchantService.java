package com.imooc.passbook.merchantplatform.service;

import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplate;
import com.imooc.passbook.merchantplatform.vo.Response;

/**
 * 商户服务接口定义
 */

public interface IMerchantService {

    // 创建商户
    Response createMerchant(CreateMerchantRequest request);

    // 根据 id 获取商户
    Response getMerchantById(Integer id);

    // 投放优惠券
    Response issuePassTemplate(PassTemplate passTemplate);
}