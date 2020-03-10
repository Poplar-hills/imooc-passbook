package com.imooc.passbook.merchantplatform.constants;

public class Constants {

  // 商户优惠券投放的 kafka topic
  public static final String TEMPLATE_TOPIC = "merchant-pass-template";

  // token key
  public static final String TOKEN_KEY = "token";

  // token value
  public static final String TOKEN_VALUE = "merchant-platform-token";

  // 优惠券 token 文件目录
  public static final String TOKEN_DIR = "/tmp/token/";
}
