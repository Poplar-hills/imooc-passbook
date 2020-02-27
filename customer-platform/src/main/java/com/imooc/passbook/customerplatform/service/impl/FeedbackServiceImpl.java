package com.imooc.passbook.customerplatform.service.impl;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.orm.FeedbackRowMapper;
import com.imooc.passbook.customerplatform.service.IFeedbackService;
import com.imooc.passbook.customerplatform.utils.RowKeyGenerator;
import com.imooc.passbook.customerplatform.vo.Feedback;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {

    private final HbaseTemplate hbaseTemplate;

    @Override
    public void createFeedback(Feedback feedback) {
        byte[] FAMILY_I = HBaseTable.FeedbackTable.FAMILY_I.getBytes();
        byte[] USER_ID = HBaseTable.FeedbackTable.USER_ID.getBytes();
        byte[] TYPE = HBaseTable.FeedbackTable.TYPE.getBytes();
        byte[] TEMPLATE_ID = HBaseTable.FeedbackTable.TEMPLATE_ID.getBytes();
        byte[] CONTENT = HBaseTable.FeedbackTable.CONTENT.getBytes();

        String rowKey = RowKeyGenerator.genForFeedback(feedback);

        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(FAMILY_I, USER_ID, Bytes.toBytes(feedback.getUserId()))
            .addColumn(FAMILY_I, TYPE, Bytes.toBytes(feedback.getType().toString()))
            .addColumn(FAMILY_I, TEMPLATE_ID, Bytes.toBytes(feedback.getTemplateId()))
            .addColumn(FAMILY_I, CONTENT, Bytes.toBytes(feedback.getContent()));

        hbaseTemplate.saveOrUpdate(HBaseTable.FeedbackTable.TABLE_NAME, put);
    }

    /*
     * 从 HBase 中查数据：
     * 1. ∵ 该方法是查找某个用户的所有 feedback ∴ 要使用 Scan 对象从 HBase 表中查找所有匹配该 userId 的记录；
     * 2. Scan 对象中可以定义不同的 filter，即以不同的方式匹配 HBase 中的记录；
     * 3. ∵ 要从 HBase 中查找一个 row key 所对应的整个 Row ∴ 要匹配的对象是 HBase 表中的 row key；
     * 4. ∵ HBase 中 Feedback 的 row key 的前缀是基于倒序的 userId 生成 ∴ 要匹配该 row key 就需要使用倒序的 userId 作为 prefix filter；
     * 5. find 方法的第3个参数可指定 RowMapper，用于将 HBase 记录反序列化成 Java 对象。
     * */
    @Override
    public List<Feedback> getFeedbackByUserId(Long userId) {
        byte[] reversedUserId = new StringBuilder(userId.toString()).reverse().toString().getBytes();
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(reversedUserId));  // 使用前缀过滤器来进行 scan
        return hbaseTemplate.find(HBaseTable.FeedbackTable.TABLE_NAME, scan, new FeedbackRowMapper());
    }
}
