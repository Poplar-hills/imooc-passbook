package com.imooc.passbook.merchantplatform.service;

import static org.mockito.Mockito.verify;

import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import com.imooc.passbook.merchantplatform.entity.Merchant;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplateRequest;
import com.imooc.passbook.merchantplatform.vo.Response;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MerchantServiceImplTest {

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Mock
    private MerchantDao merchantDao;

    @Mock
    private KafkaTemplate kafkaTemplate;

    @Captor
    private ArgumentCaptor<Merchant> merchantCaptor;

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

        merchantService.createMerchant(request);

        verify(merchantDao).save(merchantCaptor.capture());
        Merchant merchant = merchantCaptor.getValue();

        assertEquals(merchant.getName(), "Auska");
        assertEquals(merchant.getPhone(), "13212345678");
    }

    @Test
    public void should_issue_pass_template() {  // 手动测试
        PassTemplateRequest request = PassTemplateRequest.builder()
            .id(22)  // 注意这里的是商户 id ∵ 在 issuePassTemplate 时会先 validate ∴ 该 id 必须在 DB 中存在才行
            .title("通用美食券")
            .summary("简介：50%折扣")
            .desc("详情：50%折扣，不能叠加使用")
            .limit(1000L)
            .hasToken(false)
            .background(2)
            .startTime(new Date())
            .endTime(DateUtils.addDays(new Date(), 10))
            .build();

        merchantService.issuePassTemplate(request);
    }
}
