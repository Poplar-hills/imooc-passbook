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
 * 用于实现 HBase 读写 Pass Template 的 service
 */

@Slf4j
@Service
public class HbasePassTemplateServiceImpl implements IHbasePassTemplateService {

    private final HbaseTemplate hbaseTemplate;  // HBase client

    public HbasePassTemplateServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean savePassTemplateToHbase(@Valid PassTemplate passTemplate) {
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
        addColumnsForFamilyB(passTemplate, put);
        addColumnsForFamilyC(passTemplate, put);
        hbaseTemplate.saveOrUpdate(HBaseTable.PassTemplateTable.TABLE_NAME, put);  // 真正存入 HBase
        return true;
    }

    private void addColumnsForFamilyB(PassTemplate passTemplate, Put put) {
        String family = HBaseTable.PassTemplateTable.FAMILY_B;

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.ID),
            Bytes.toBytes(passTemplate.getId())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.TITLE),
            Bytes.toBytes(passTemplate.getTitle())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.SUMMARY),
            Bytes.toBytes(passTemplate.getSummary())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.DESC),
            Bytes.toBytes(passTemplate.getDesc())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.HAS_TOKEN),
            Bytes.toBytes(passTemplate.getHasToken())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.BACKGROUND),
            Bytes.toBytes(passTemplate.getBackground())
        );
    }

    private void addColumnsForFamilyC(PassTemplate passTemplate, Put put) {
        String family = HBaseTable.PassTemplateTable.FAMILY_C;

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
            Bytes.toBytes(passTemplate.getLimit())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.START_TIME),
            Bytes.toBytes(passTemplate.getStartTime().toString())
        );

        put.addColumn(
            Bytes.toBytes(family),
            Bytes.toBytes(HBaseTable.PassTemplateTable.END_TIME),
            Bytes.toBytes(passTemplate.getEndTime().toString())
        );
    }
}
