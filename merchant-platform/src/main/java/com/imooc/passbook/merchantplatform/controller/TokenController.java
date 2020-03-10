package com.imooc.passbook.merchantplatform.controller;

import com.imooc.passbook.merchantplatform.service.ITokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 商户上传 token 的 controller
 */

@Slf4j
@RestController
@AllArgsConstructor
public class TokenController {

    private final ITokenService tokenService;

    @PostMapping("/token")
    public void tokenFileUpload(@RequestParam("merchantsId") @NotBlank String merchantsId,
                                @RequestParam("passTemplateId") @NotBlank String passTemplateId,
                                @RequestParam("file") MultipartFile file) {
        tokenService.uploadTokenFile(merchantsId, passTemplateId, file);
    }
}
