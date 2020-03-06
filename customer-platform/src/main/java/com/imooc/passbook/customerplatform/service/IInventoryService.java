package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.AvailablePassTemplates;

public interface IInventoryService {

    // 获取用户可领取的优惠券
    AvailablePassTemplates getAvailablePassTemplates(Long userId) throws Exception;
}
