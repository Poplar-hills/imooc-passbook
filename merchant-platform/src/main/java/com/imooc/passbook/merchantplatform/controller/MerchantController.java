package com.imooc.passbook.merchantplatform.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook.merchantplatform.service.IMerchantService;
import com.imooc.passbook.merchantplatform.vo.CreateMerchantRequest;
import com.imooc.passbook.merchantplatform.vo.PassTemplate;
import com.imooc.passbook.merchantplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final IMerchantService merchantService;

    @Autowired
    public MerchantController(IMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public Response createMerchant(@RequestBody CreateMerchantRequest request) {
        log.info("📮 [MerchantController] createMerchant request: {}", JSON.toJSONString(request));
        return merchantService.createMerchant(request);
    }

    @GetMapping("/{id}")
    public Response getMerchant(@PathVariable Integer id) {
        log.info("📮 [MerchantController] getMerchant with id: {}", id);
        return merchantService.getMerchantById(id);
    }

    @PostMapping("/pass-templates")
    public Response issuePassTemplate(@RequestBody PassTemplate passTemplate) {
        log.info("📮 [MerchantController] issuePassTemplate: {}", passTemplate);
        return merchantService.issuePassTemplate(passTemplate);
    }



}
