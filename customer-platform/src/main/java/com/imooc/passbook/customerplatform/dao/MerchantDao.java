package com.imooc.passbook.customerplatform.dao;

import com.imooc.passbook.customerplatform.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Merchant 的 Data Access Object
 */

public interface MerchantDao extends JpaRepository<Merchant, Integer> {  // 注意该接口需继承 JpaRepository<T,ID>

    Optional<Merchant> findById(Integer id);

    Optional<Merchant> findByName(String name);

    List<Merchant> findByIdIn(List<Integer> ids);
}
