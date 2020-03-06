package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.Pass;
import com.imooc.passbook.customerplatform.vo.PassInfo;

import java.util.List;

public interface IUserPassService {

    // 获取用户已领取的优惠券（"我的优惠券"中的功能）
    List<PassInfo> getUnusedPassInfos(Long userId) throws Exception;

    // 获取用户已消费的优惠券（"我的优惠券"中的功能）
    List<PassInfo> getUsedPasseInfos(Long userId) throws Exception;

    // 获取用户所有的优惠券（"我的优惠券"中的功能）
    List<PassInfo> getAllUserPasseInfos(Long userId) throws Exception;

    // 消费优惠券
    void consumePass(Pass pass);
}
