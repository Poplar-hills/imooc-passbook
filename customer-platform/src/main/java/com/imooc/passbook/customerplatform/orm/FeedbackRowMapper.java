package com.imooc.passbook.customerplatform.orm;

import com.imooc.passbook.customerplatform.constants.FeedbackType;
import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.vo.Feedback;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * ORM Mapper for mapping HBase Feedback row to Java Feedback object
 */

public class FeedbackRowMapper implements RowMapper<Feedback> {

    private static byte[] FAMILY_I = HBaseTable.FeedbackTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = HBaseTable.FeedbackTable.USER_ID.getBytes();
    private static byte[] TYPE = HBaseTable.FeedbackTable.TYPE.getBytes();
    private static byte[] TEMPLATE_ID = HBaseTable.FeedbackTable.TEMPLATE_ID.getBytes();
    private static byte[] CONTENT = HBaseTable.FeedbackTable.CONTENT.getBytes();

    @Override
    public Feedback mapRow(Result result, int rowNum) throws Exception {
        String typeStr = Bytes.toString(result.getValue(FAMILY_I, TYPE));

        return Feedback.builder()
            .userId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)))
            .type(FeedbackType.valueOf(typeStr))  // 将 String 对应到 enum 类上的方法
            .templateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)))
            .content(Bytes.toString(result.getValue(FAMILY_I, CONTENT)))
            .build();
    }
}
