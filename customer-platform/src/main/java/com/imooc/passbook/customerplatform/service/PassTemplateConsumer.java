package com.imooc.passbook.customerplatform.service;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook.customerplatform.constants.Constants;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * 用于消费 pass template 的 Kafka consumer
 */

@Slf4j
@Service
@AllArgsConstructor
public class PassTemplateConsumer {

    private final IPassTemplateService passTemplateService;

    /**
     * 接收 Kafka 消息的回调方法
     * 1. 接收 Kafka 消息
     * 2. 将消息中的 passTemplate string 反序列化为 PassTemplate 对象
     * 3. 将 PassTemplate 对象存入 HBase
     * - @KafkaListener 让 Spring 知道该 bean 是个 kafka listener，并按照参数中的注解（@Payload, @Header）注入参数。
     */
    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void onReceive(@Payload String passTemplateStr,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("[Kafka consumer] receives pass template: {}", passTemplateStr);

        PassTemplate passTemplate;
        try {
            passTemplate = JSON.parseObject(passTemplateStr, PassTemplate.class);
        } catch (Exception e) {
            log.error("[Kafka consumer] parse pass template error: {}", e.getMessage());
            return;
        }

        passTemplateService.createPassTemplate(passTemplate);
        log.info("[Kafka consumer] saved pass template to HBase: {}", passTemplate);
    }
}
