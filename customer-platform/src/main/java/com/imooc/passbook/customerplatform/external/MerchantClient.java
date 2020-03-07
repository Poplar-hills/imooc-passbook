package com.imooc.passbook.customerplatform.external;

import com.imooc.passbook.customerplatform.entity.Merchant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//@FeignClient(   // TODO: import this class
//    value = "${external.merchant-platform.name:merchant-platform}",
//    url = "${external.merchant-platform.url}")
public interface MerchantClient {

    @GetMapping(
        value = "/v1/merchants}",
        consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Merchant> getMerchants(List<Integer> merchantIds);
}
