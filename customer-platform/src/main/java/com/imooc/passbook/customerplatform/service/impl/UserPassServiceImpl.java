package com.imooc.passbook.customerplatform.service.impl;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.dao.MerchantDao;
import com.imooc.passbook.customerplatform.entity.Merchant;
import com.imooc.passbook.customerplatform.service.IUserPassService;
import com.imooc.passbook.customerplatform.vo.CollectablePassTemplates;
import com.imooc.passbook.customerplatform.vo.Pass;
import com.imooc.passbook.customerplatform.vo.PassInfo;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserPassServiceImpl implements IUserPassService {

    private final HbaseTemplate hbaseTemplate;
    private final MerchantDao merchantsDao;

    @Override
    public List<PassInfo> getCollectedPassInfos(Long userId) throws Exception {
        return null;
    }

    @Override
    public List<PassInfo> getConsumedPasseInfos(Long userId) throws Exception {
        return null;
    }

    @Override
    public List<PassInfo> getAllUserPasseInfos(Long userId) throws Exception {
        return null;
    }

    @Override
    public CollectablePassTemplates getCollectablePassTemplates(Long userId) throws Exception {
        return null;
    }

    @Override
    public void consumePass(Pass pass) {

    }

    // 通过 Pass 对象构造 PassTemplate Map
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        byte[] FAMILY_B = HBaseTable.PassTemplateTable.FAMILY_B.getBytes();
        byte[] ID = HBaseTable.PassTemplateTable.ID.getBytes();
        byte[] TITLE = HBaseTable.PassTemplateTable.TITLE.getBytes();
        byte[] SUMMARY = HBaseTable.PassTemplateTable.SUMMARY.getBytes();
        byte[] DESC = HBaseTable.PassTemplateTable.DESC.getBytes();
        byte[] HAS_TOKEN = HBaseTable.PassTemplateTable.HAS_TOKEN.getBytes();
        byte[] BACKGROUND = HBaseTable.PassTemplateTable.BACKGROUND.getBytes();

        byte[] FAMILY_C = HBaseTable.PassTemplateTable.FAMILY_C.getBytes();
        byte[] LIMIT = HBaseTable.PassTemplateTable.LIMIT.getBytes();
        byte[] START_TIME = HBaseTable.PassTemplateTable.START_TIME.getBytes();
        byte[] END_TIME = HBaseTable.PassTemplateTable.END_TIME.getBytes();

        List<Get> templateGets = passes.stream()
            .map(Pass::getTemplateId)
            .map(id -> new Get(id.getBytes()))
            .collect(Collectors.toList());

        Result[] templateResults = hbaseTemplate.getConnection()
            .getTable(TableName.valueOf(HBaseTable.PassTemplateTable.TABLE_NAME))
            .get(templateGets);

        return Arrays.stream(templateResults)
            .collect(Collectors.toMap(
                (Result r) -> Bytes.toString(r.getRow()),   // keyMapper
                (Result r) -> PassTemplate.builder()        // valueMapper
                    .id(Bytes.toInt(r.getValue(FAMILY_B, ID)))
                    .title(Bytes.toString(r.getValue(FAMILY_B, TITLE)))
                    .summary(Bytes.toString(r.getValue(FAMILY_B, SUMMARY)))
                    .desc(Bytes.toString(r.getValue(FAMILY_B, DESC)))
                    .hasToken(Bytes.toBoolean(r.getValue(FAMILY_B, HAS_TOKEN)))
                    .background(Bytes.toInt(r.getValue(FAMILY_B, BACKGROUND)))
                    .limit(Bytes.toLong(r.getValue(FAMILY_C, LIMIT)))
                    .startTime(LocalDateTime.parse(Bytes.toString(r.getValue(FAMILY_C, START_TIME)), formatter))
                    .endTime(LocalDateTime.parse(Bytes.toString(r.getValue(FAMILY_C, END_TIME)), formatter))
                    .build()
            ));
    }

    // 通过 PassTemplate 对象构造 Merchant Map
    private Map<Integer, Merchant> buildMerchantMap(List<PassTemplate> passTemplates) {
        List<Integer> merchantsIds = passTemplates.stream()
            .map(PassTemplate::getId)
            .collect(Collectors.toList());

        return merchantsDao.findByIdIn(merchantsIds).stream()
            .collect(Collectors.toMap(Merchant::getId, merchant -> merchant));
    }
}
