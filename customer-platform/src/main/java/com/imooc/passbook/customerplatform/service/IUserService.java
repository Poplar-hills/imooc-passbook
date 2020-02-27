package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.User;

public interface IUserService {

    // 创建用户
    User createUser(User user) throws Exception;
}
