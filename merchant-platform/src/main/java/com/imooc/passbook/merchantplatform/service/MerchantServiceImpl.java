package com.imooc.passbook.merchantplatform.service;

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
        Response response = new Response();
        ErrorCode errorCode = request.validate(merchantDao);  // 检查请求中是否提供了有效信息

        if (errorCode == ErrorCode.SUCCESS) {  // 若请求有效
            Merchant merchant = request.toMerchant();
            Merchant savedMerchant = (Merchant) merchantDao.save(merchant);
            response.setData(savedMerchant);
        } else {
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }

        return response;
    }

    @Override
    public Response getMerchantById(Integer id) {
        Response response = new Response();
        Optional<Merchant> merchant = merchantDao.findById(id);

        if (!merchant.isPresent()) {
            response.setErrorCode(ErrorCode.MERCHANT_NOT_EXIST.getCode());
            response.setErrorMsg(ErrorCode.MERCHANT_NOT_EXIST.getDesc());
        }
        response.setData(merchant);

        return response;
    }

    @Override
    public Response issuePassTemplate(PassTemplate passTemplate) {
        Response response = new Response();
        ErrorCode errorCode = passTemplate.validate(merchantDao);


        return response;
    }

}