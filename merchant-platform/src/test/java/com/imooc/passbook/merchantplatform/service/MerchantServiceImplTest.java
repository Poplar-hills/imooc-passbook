package com.imooc.passbook.merchantplatform.service;

import com.imooc.passbook.merchantplatform.entity.Merchant;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MerchantServiceImplTest {

    @Autowired
    private IMerchantService merchantService;

    @Test
    @Transactional  // 该注解让 Spring boot test 在测试运行结束后恢复 DB 的数据状态，保持与测试运行之前一致
    public void should_create_merchant_successfully() {
        CreateMerchantRequest request = CreateMerchantRequest.builder()
            .name("Auska")
            .logoUrl("www.abc.com")
            .businessLicenseUrl("www.123.com")
            .address("14 Honeysuckle Road")
            .phone("13212345678")
            .build();

        Response res = merchantService.createMerchant(request);
        Merchant merchant = (Merchant) res.getData();

        assertEquals(merchant.getName(), "Auska");
        assertEquals(merchant.getPhone(), "13212345678");
    }
}
