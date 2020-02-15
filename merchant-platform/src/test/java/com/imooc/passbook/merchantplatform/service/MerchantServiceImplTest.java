package com.imooc.passbook.merchantplatform.service;

import static org.mockito.Mockito.verify;

import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import com.imooc.passbook.merchantplatform.entity.Merchant;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplateRequest;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MerchantServiceImplTest {

  @InjectMocks
  private MerchantServiceImpl merchantService;

  @Mock
  private MerchantDao merchantDao;

  @Captor
  private ArgumentCaptor<Merchant> merchantCaptor;

  @Test
  @Transactional  // 该注解让 Spring boot test 在测试运行结束后恢复 DB 的数据状态，保持与测试运行之前一致
  public void should_create_merchant() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("Auska")
        .logoUrl("www.abc.com")
        .businessLicenseUrl("www.123.com")
        .address("14 Honeysuckle Road")
        .phone("13212345678")
        .build();

    merchantService.createMerchant(request);

    verify(merchantDao).save(merchantCaptor.capture());
    Merchant merchant = merchantCaptor.getValue();

    assertEquals(merchant.getName(), "Auska");
    assertEquals(merchant.getPhone(), "13212345678");
  }

}
