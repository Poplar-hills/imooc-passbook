package com.imooc.passbook.merchantplatform.service;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook.merchantplatform.constants.Constants;
import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import com.imooc.passbook.merchantplatform.entity.Merchant;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplate;
import com.imooc.passbook.merchantplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 商户服务接口实现
 */

@Slf4j
@Service
public class MerchantServiceImpl implements IMerchantService {

    private final MerchantDao merchantDao;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MerchantServiceImpl(MerchantDao merchantDao, KafkaTemplate<String, String> kafkaTemplate) {
        this.merchantDao = merchantDao;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional   // 事务方法
    public Response createMerchant(CreateMerchantRequest request) {
        ErrorCode errorCode = request.validate(merchantDao);  // 检查请求是否有效
        if (errorCode != ErrorCode.SUCCESS)
            return Response.from(errorCode);

        Response response = new Response();
        Merchant merchant = request.toMerchant();
        Merchant savedMerchant = merchantDao.save(merchant);
        response.setData(savedMerchant);
        return response;
    }

    @Override
    public Response getMerchantById(Integer id) {
        Response response = new Response();
        Optional<Merchant> merchant = merchantDao.findById(id);

        if (!merchant.isPresent())
            response.setError(ErrorCode.MERCHANT_NOT_EXIST);

        response.setData(merchant);
        return response;
    }

    @Override
    public Response issuePassTemplate(PassTemplate passTemplate) {
        Response response = new Response();
        ErrorCode errorCode = passTemplate.validate(merchantDao);  // 先验证优惠券的有效性

        if (errorCode != ErrorCode.SUCCESS)
            response.setError(errorCode);
        else {
            String passTemplateStr = JSON.toJSONString(passTemplate);
            kafkaTemplate.send(            // 通过 Kafka 发送消息
                Constants.TEMPLATE_TOPIC,  // Kafka topic
                Constants.TEMPLATE_TOPIC,  // message key
                passTemplateStr);          // message value

            log.info("📮 [issuePassTemplate] issued a passTemplate: {}", passTemplate);
        }

        return response;
    }

}