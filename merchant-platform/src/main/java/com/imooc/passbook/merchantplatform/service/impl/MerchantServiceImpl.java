package com.imooc.passbook.merchantplatform.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook.merchantplatform.constants.Constants;
import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import com.imooc.passbook.merchantplatform.entity.Merchant;
import com.imooc.passbook.merchantplatform.service.IMerchantService;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplateRequest;
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

  private final KafkaTemplate<String, String> kafkaTemplate;  // 注入 Kafka client

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

    Merchant merchant = Merchant.from(request);
    merchantDao.save(merchant);
    return new Response();  // TODO: 给响应添加成功的 message
  }

  @Override
  public Response getMerchantById(Integer id) {
    Optional<Merchant> merchant = merchantDao.findById(id);

    if (!merchant.isPresent())
      return Response.from(ErrorCode.MERCHANT_NOT_EXIST);

    Response response = new Response();
    response.setData(merchant);
    return response;
  }

  @Override
  public Response issuePassTemplate(PassTemplateRequest request) {
    ErrorCode errorCode = request.validate(merchantDao);  // 先验证优惠券的有效性

    if (errorCode != ErrorCode.SUCCESS)
      return Response.from(errorCode);

    String passTemplateStr = JSON.toJSONString(request);
    kafkaTemplate.send(            // 通过 Kafka client 向 broker 发送消息（进去看一下 send 源码，还可指定 partition）
        Constants.TEMPLATE_TOPIC,  // Kafka topic
        Constants.TEMPLATE_TOPIC,  // message key
        passTemplateStr);          // message value

    log.info("📮 [issuePassTemplate] issued a passTemplate: {}", request);

    return new Response();
  }

}