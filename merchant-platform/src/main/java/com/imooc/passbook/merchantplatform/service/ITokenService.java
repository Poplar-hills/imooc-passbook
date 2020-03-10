package com.imooc.passbook.merchantplatform.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 商户 token 服务接口定义
 */

public interface ITokenService {

    void uploadTokenFile(String merchantsId, String passTemplateId, MultipartFile file);
}
