package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.Pass;
import com.imooc.passbook.customerplatform.vo.PassInfo;

import java.util.List;

public interface IUserPassService {

    // 获取用户已领取但未使用的优惠券
    List<PassInfo> getUnusedPassInfos(Long userId) throws Exception;

    // 获取用户已领取且已使用的优惠券
    List<PassInfo> getUsedPasseInfos(Long userId) throws Exception;

    // 获取用户所有已领取的优惠券
    List<PassInfo> getAllUserPassInfos(Long userId) throws Exception;

    // 消费优惠券
    void consumePass(Pass pass);
}
