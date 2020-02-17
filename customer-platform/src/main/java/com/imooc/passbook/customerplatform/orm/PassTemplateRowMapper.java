package com.imooc.passbook.customerplatform.orm;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ORM Mapper for mapping HBase PassTemplate row to Java PassTemplate object
 */

public class PassTemplateRowMapper implements RowMapper<PassTemplate> {

    private static byte[] FAMILY_B = HBaseTable.PassTemplateTable.FAMILY_B.getBytes();
    private static byte[] ID = HBaseTable.PassTemplateTable.ID.getBytes();
    private static byte[] TITLE = HBaseTable.PassTemplateTable.TITLE.getBytes();
    private static byte[] SUMMARY = HBaseTable.PassTemplateTable.SUMMARY.getBytes();
    private static byte[] DESC = HBaseTable.PassTemplateTable.DESC.getBytes();
    private static byte[] HAS_TOKEN = HBaseTable.PassTemplateTable.HAS_TOKEN.getBytes();
    private static byte[] BACKGROUND = HBaseTable.PassTemplateTable.BACKGROUND.getBytes();

    private static byte[] FAMILY_C = HBaseTable.PassTemplateTable.FAMILY_C.getBytes();
    private static byte[] LIMIT = HBaseTable.PassTemplateTable.LIMIT.getBytes();
    private static byte[] START_TIME = HBaseTable.PassTemplateTable.START_TIME.getBytes();
    private static byte[] END_TIME = HBaseTable.PassTemplateTable.END_TIME.getBytes();

    @Override
    public PassTemplate mapRow(Result result, int rowNum) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return PassTemplate.builder()
            .id(Bytes.toInt(result.getRow()))
            .title(Bytes.toString(result.getValue(FAMILY_B, TITLE)))
            .summary(Bytes.toString(result.getValue(FAMILY_B, SUMMARY)))
            .desc(Bytes.toString(result.getValue(FAMILY_B, DESC)))
            .hasToken(Bytes.toBoolean(result.getValue(FAMILY_B, HAS_TOKEN)))
            .background(Bytes.toInt(result.getValue(FAMILY_B, BACKGROUND)))
            .limit(Bytes.toLong(result.getValue(FAMILY_C, LIMIT)))
            .startTime(LocalDateTime.parse(Bytes.toString(result.getValue(FAMILY_C, START_TIME)), formatter))
            .endTime(LocalDateTime.parse(Bytes.toString(result.getValue(FAMILY_C, END_TIME)), formatter))
            .build();
    }
}
