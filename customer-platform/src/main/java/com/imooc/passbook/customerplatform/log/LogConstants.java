package com.imooc.passbook.customerplatform.log;

/**
 * 日志记录常量定义
 */

public class LogConstants {

    public class UserAction {

        // 查看优惠券信息
        public static final String GET_PASS_INFO = "GetPassInfo";

        // 查看已使用的优惠券信息
        public static final String GET_USED_PASS_INFO = "GetUsedPassInfo";

        // 使用优惠券
        public static final String USE_PASS = "UserUsePass";

        // 获取库存信息
        public static final String GET_INVENTORY_INFO = "GetInventoryInfo";

        // 领取优惠券
        public static final String GET_PASS_TEMPLATE = "GetPassTemplate";

        // 创建评论
        public static final String CREATE_FEEDBACK = "CreateFeedback";

        // 获取评论
        public static final String GET_FEEDBACK = "GetFeedback";

        // 创建用户
        public static final String CREATE_USER = "CreateUser";
    }
}
