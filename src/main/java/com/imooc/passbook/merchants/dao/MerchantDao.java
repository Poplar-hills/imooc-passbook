package com.imooc.passbook.merchants.dao;

import com.imooc.passbook.merchants.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Merchant 的 Data Access Object
 */

public interface MerchantDao extends JpaRepository {  // 注意该接口需继承 JpaRepository

    Merchant findById(Integer id);

    Merchant findByName(String name);
}
