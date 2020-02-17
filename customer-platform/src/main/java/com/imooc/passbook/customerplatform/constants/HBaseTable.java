package com.imooc.passbook.customerplatform.constants;

/**
 * HBase 中各个表的 Column Qualifier 声明
 * - ∵ HBase 在创建表时无需确定列名 ∴ 在 passbook.hsh 文件中没有对列的声明，而是在这里声明
 */

public class HBaseTable {

    // HBase 中的 User 表的结构声明
    public class UserTable {

        // 表名
        public static final String TABLE_NAME = "pb:user";

        // 基本信息列族（basic）
        public static final String FAMILY_B = "b";

        // 用户名
        public static final String NAME = "name";

        // 用户年龄
        public static final String AGE = "age";

        // 用户性别
        public static final String SEX = "sex";

        // 额外信息列族
        public static final String FAMILY_O = "o";

        // 电话号码
        public static final String PHONE = "phone";

        // 住址
        public static final String ADDRESS = "address";
    }

    // HBase 中的 PassTemplate 表的结构声明
    public class PassTemplateTable {

        // PassTemplate HBase 表名
        public static final String TABLE_NAME = "pb:passtemplate";

        // 基本信息列族
        public static final String FAMILY_B = "b";

        // 商户 id
        public static final String ID = "id";

        // 优惠券标题
        public static final String TITLE = "title";

        // 优惠券摘要信息
        public static final String SUMMARY = "summary";

        // 优惠券详细信息
        public static final String DESC = "desc";

        // 优惠券是否有 token
        public static final String HAS_TOKEN = "has_token";

        // 优惠券背景色
        public static final String BACKGROUND = "background";

        // 约束信息列族
        public static final String FAMILY_C = "c";

        // 最大个数限制
        public static final String LIMIT = "limit";

        // 优惠券开始时间
        public static final String START_TIME = "start";

        // 优惠券结束时间
        public static final String END_TIME = "end";
    }

    // HBase 中的 Pass 表的结构声明
    public class PassTable {

        // Pass HBase 表名
        public static final String TABLE_NAME = "pb:pass";

        // 信息列族
        public static final String FAMILY_I = "i";

        // 用户 id
        public static final String USER_ID = "user_id";

        // 优惠券 id
        public static final String TEMPLATE_ID = "template_id";

        // 优惠券识别码
        public static final String TOKEN = "token";

        // 领取日期
        public static final String COLLECT_DATE = "assigned_date";

        // 消费日期
        public static final String CONSUME_DATE = "con_date";
    }

    // HBase 中的 Feedback 表的结构声明
    public class Feedback {

        // Feedback HBase 表名
        public static final String TABLE_NAME = "pb:feedback";

        // 信息列族
        public static final String FAMILY_I = "i";

        // 用户 id
        public static final String USER_ID = "user_id";

        // 评论类型
        public static final String TYPE = "type";

        // PassTemplate RowKey, 如果是 app 评论, 则是 -1
        public static final String TEMPLATE_ID = "template_id";

        // 评论内容
        public static final String CONTENT = "comment";
    }
}
