package com.imooc.passbook.customerplatform.orm;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.vo.Pass;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ORM Mapper for mapping HBase Pass row to Java Pass object
 */

public class PassRowMapper implements RowMapper<Pass> {

    private static byte[] FAMILY_I = HBaseTable.PassTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = HBaseTable.PassTable.USER_ID.getBytes();
    private static byte[] TEMPLATE_ID = HBaseTable.PassTable.TEMPLATE_ID.getBytes();
    private static byte[] TOKEN = HBaseTable.PassTable.TOKEN.getBytes();
    private static byte[] COLLECT_DATE = HBaseTable.PassTable.COLLECT_DATE.getBytes();
    private static byte[] CONSUME_DATE = HBaseTable.PassTable.CONSUME_DATE.getBytes();

    @Override
    public Pass mapRow(Result result, int rowNum) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String consumeDateStr = Bytes.toString(result.getValue(FAMILY_I, CONSUME_DATE));

        return Pass.builder()
            .userId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)))
            .templateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)))
            .token(Bytes.toString(result.getValue(FAMILY_I, TOKEN)))
            .collectDate(LocalDateTime.parse(Bytes.toString(result.getValue(FAMILY_I, COLLECT_DATE)), formatter))
            .consumeDate(consumeDateStr.equals("-1") ? null : LocalDateTime.parse(consumeDateStr, formatter))
            .build();
    }
}
