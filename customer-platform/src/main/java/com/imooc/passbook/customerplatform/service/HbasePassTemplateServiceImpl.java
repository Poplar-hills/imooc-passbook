package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.utils.RowKeyGenerator;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;

/**
 * 用于在 HBase 中读写 Pass Template 的 service
 */

@Slf4j
@Service
public class HbasePassTemplateServiceImpl implements IHbasePassTemplateService {

    private final HbaseTemplate hbaseTemplate;  // HBase client

    public HbasePassTemplateServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean createPassTemplate(@Valid PassTemplate passTemplate) {
        String rowKey = RowKeyGenerator.genForPassTemplate(passTemplate);
        String title = passTemplate.getTitle();

        try {
            if (!isRowKeyValid(rowKey)) {  // 验证 row key 在 PassTemplateTable 中是唯一的
                log.warn("Row key {} already exist", rowKey);
                return false;
            }
            if (!isTitleValid(title)) {  // 验证 pass template title 在 HBase 中是唯一的
                log.warn("Pass template title {} already exist", title);
                return false;
            }
        } catch (Exception e) {
            log.error("[savePassTemplateToHbase] error: {}", e.getMessage());
            return false;
        }

        return saveToHbase(rowKey, passTemplate);
    }

    private boolean isRowKeyValid(String rowKey) throws IOException {
        Connection connection = hbaseTemplate.getConnection();
        TableName tableName = TableName.valueOf(HBaseTable.PassTemplateTable.TABLE_NAME);
        Table table = connection.getTable(tableName);
        Get getRowKeyQuery = new Get(Bytes.toBytes(rowKey));
        return !table.exists(getRowKeyQuery);
    }

    private boolean isTitleValid(String title) throws IOException {
        Connection connection = hbaseTemplate.getConnection();
        TableName tableName = TableName.valueOf(HBaseTable.PassTemplateTable.TABLE_NAME);
        Table table = connection.getTable(tableName);
        Get getTitleQuery = new Get(Bytes.toBytes(title));  // TODO: 如何在 HBase 表中验证 title 唯一？
        return !table.exists(getTitleQuery);
    }

    private boolean saveToHbase(String rowKey, PassTemplate passTemplate) {
        Put put = new Put(Bytes.toBytes(rowKey));
        put = addColumnsForFamilyB(passTemplate, put);
        put = addColumnsForFamilyC(passTemplate, put);
        hbaseTemplate.saveOrUpdate(HBaseTable.PassTemplateTable.TABLE_NAME, put);  // 真正存入 HBase
        return true;
    }

    private Put addColumnsForFamilyB(PassTemplate passTemplate, Put put) {
        byte[] family = HBaseTable.PassTemplateTable.FAMILY_B.getBytes();
        byte[] id = HBaseTable.PassTemplateTable.ID.getBytes();
        byte[] title = HBaseTable.PassTemplateTable.TITLE.getBytes();
        byte[] summary = HBaseTable.PassTemplateTable.SUMMARY.getBytes();
        byte[] desc = HBaseTable.PassTemplateTable.DESC.getBytes();
        byte[] hasToken = HBaseTable.PassTemplateTable.HAS_TOKEN.getBytes();
        byte[] background = HBaseTable.PassTemplateTable.BACKGROUND.getBytes();

        return put.addColumn(family, id, Bytes.toBytes(passTemplate.getId()))
            .addColumn(family, title, Bytes.toBytes(passTemplate.getTitle()))
            .addColumn(family, summary, Bytes.toBytes(passTemplate.getSummary()))
            .addColumn(family, desc, Bytes.toBytes(passTemplate.getDesc()))
            .addColumn(family, hasToken, Bytes.toBytes(passTemplate.getHasToken()))
            .addColumn(family, background, Bytes.toBytes(passTemplate.getBackground()));
    }

    private Put addColumnsForFamilyC(PassTemplate passTemplate, Put put) {
        byte[] family = HBaseTable.PassTemplateTable.FAMILY_C.getBytes();
        byte[] limit = HBaseTable.PassTemplateTable.LIMIT.getBytes();
        byte[] startTime = HBaseTable.PassTemplateTable.START_TIME.getBytes();
        byte[] endTime = HBaseTable.PassTemplateTable.END_TIME.getBytes();

        return put.addColumn(family, limit, Bytes.toBytes(passTemplate.getLimit()))
            .addColumn(family, startTime, Bytes.toBytes(passTemplate.getStartTime().toString()))
            .addColumn(family, endTime, Bytes.toBytes(passTemplate.getEndTime().toString()));
    }
}
