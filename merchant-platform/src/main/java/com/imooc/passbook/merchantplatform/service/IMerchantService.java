package com.imooc.passbook.merchantplatform.service;

import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplateRequest;
import com.imooc.passbook.merchantplatform.vo.Response;

/**
 * 商户服务接口定义
 *
 * - What's the point of having every service class have an interface?
 * 1. following the SOLID principles and targeting an interface rather than concrete class.
 * 2. provide you with another mock implementation for unit tests.
 */

public interface IMerchantService {

  // 创建商户
  Response createMerchant(CreateMerchantRequest request);

  // 根据 id 获取商户
  Response getMerchantById(Integer id);

  // 投放优惠券
  Response issuePassTemplate(PassTemplateRequest passTemplateRequest);
}