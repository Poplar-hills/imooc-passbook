package com.imooc.passbook.customerplatform.constants;

public class Constants {

    // 商户优惠券投放的 kafka topic
    public static final String TEMPLATE_TOPIC = "merchant-pass-template";

    // 已使用的 token 的文件名后缀
    public static final String USED_TOKEN_SUFFIX = "_used";

    // 用户数的 redis key
    public static final String USER_COUNT_REDIS_KEY = "customer-platform-user-count";

    // token key
    public static final String TOKEN_KEY = "token";

    // token value
    public static final String TOKEN_VALUE = "customer-platform-token";
}
