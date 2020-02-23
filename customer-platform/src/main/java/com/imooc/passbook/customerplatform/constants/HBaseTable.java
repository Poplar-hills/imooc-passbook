package com.imooc.passbook.customerplatform.constants;

/**
 * HBase 中各个表的 Column Qualifier 声明
 * - ∵ HBase 在创建表时无需确定列名 ∴ 在 passbook.hsh 文件中没有对列的声明，而是在这里声明
 */

public class HBaseTable {

    // User 表的结构声明
    public class UserTable {
        public static final String TABLE_NAME = "pb:user";

        public static final String FAMILY_B = "b";  // 基本信息列族（basic）
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String SEX = "sex";

        public static final String FAMILY_O = "o";  // 额外信息列族
        public static final String PHONE = "phone";
        public static final String ADDRESS = "address";
    }

    // PassTemplate 表的结构声明
    public class PassTemplateTable {
        public static final String TABLE_NAME = "pb:passtemplate";

        public static final String FAMILY_B = "b";  // 基本信息列族
        public static final String ID = "id";  // 商户 id
        public static final String TITLE = "title";  // 优惠券标题
        public static final String SUMMARY = "summary";  // 优惠券摘要信息
        public static final String DESC = "desc";  // 优惠券详细信息
        public static final String HAS_TOKEN = "has_token";  // 优惠券是否有 token
        public static final String BACKGROUND = "background";  // 优惠券背景色

        public static final String FAMILY_C = "c";  // 约束信息列族
        public static final String LIMIT = "limit";  // 最大个数限制
        public static final String START_TIME = "start_time";  // 优惠券开始时间
        public static final String END_TIME = "end_time";  // 优惠券结束时间
    }

    // Pass 表的结构声明
    public class PassTable {
        public static final String TABLE_NAME = "pb:pass";

        public static final String FAMILY_I = "i";  // 信息列族
        public static final String USER_ID = "user_id";
        public static final String TEMPLATE_ID = "template_id";  // 优惠券 id
        public static final String TOKEN = "token";  // 优惠券识别码
        public static final String COLLECT_DATE = "collect_date";  // 领取日期
        public static final String CONSUME_DATE = "consume_date";  // 消费日期
    }

    // Feedback 表的结构声明
    public class FeedbackTable {
        public static final String TABLE_NAME = "pb:feedback";

        public static final String FAMILY_I = "i";  // 信息列族
        public static final String USER_ID = "user_id";
        public static final String TYPE = "type";  // 评论类型（app or pass template）
        public static final String TEMPLATE_ID = "template_id";  // PassTemplate 的 RowKey, 如果是 app 评论, 则是 -1
        public static final String CONTENT = "comment";  // 评论内容
    }
}
