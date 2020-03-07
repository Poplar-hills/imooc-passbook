package com.imooc.passbook.customerplatform.service.impl;

import com.imooc.passbook.customerplatform.constants.ErrorCode;
import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.constants.PassStatus;
import com.imooc.passbook.customerplatform.entity.Merchant;
import com.imooc.passbook.customerplatform.exception.BusinessException;
import com.imooc.passbook.customerplatform.external.MerchantClient;
import com.imooc.passbook.customerplatform.orm.PassRowMapper;
import com.imooc.passbook.customerplatform.service.IUserPassService;
import com.imooc.passbook.customerplatform.vo.Pass;
import com.imooc.passbook.customerplatform.vo.PassInfo;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserPassServiceImpl implements IUserPassService {

    private final HbaseTemplate hbaseTemplate;
    private final MerchantClient merchantClient;

    // 获取用户已领取但未使用的优惠券
    @Override
    public List<PassInfo> getUnusedPassInfos(Long userId) throws Exception {
        return getPassInfosByStatus(userId, PassStatus.UNUSED);
    }

    // 获取用户已领取且已使用的优惠券
    @Override
    public List<PassInfo> getUsedPasseInfos(Long userId) throws Exception {
        return getPassInfosByStatus(userId, PassStatus.USED);
    }

    // 获取用户所有已领取的优惠券
    @Override
    public List<PassInfo> getAllUserPassInfos(Long userId) throws Exception {
        return getPassInfosByStatus(userId, PassStatus.ALL);
    }

    // 消费优惠券
    @Override
    public void consumePass(Pass pass) {
        if (!isPassValid(pass)) {
            log.error("[consumePass] invalid pass: {}", pass);
            throw new BusinessException(ErrorCode.INVALID_PASS);
        }

        byte[] FAMILY_I = HBaseTable.PassTable.FAMILY_I.getBytes();
        byte[] CONSUME_DATE = HBaseTable.PassTable.CONSUME_DATE.getBytes();

        Put put = new Put(pass.getRowKey().getBytes());
        put.addColumn(FAMILY_I, CONSUME_DATE, Bytes.toBytes(LocalDate.now().toString()));
        hbaseTemplate.saveOrUpdate(HBaseTable.PassTable.TABLE_NAME, put);
    }

    private boolean isPassValid(Pass pass) {
        String reversedUserId = new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString();
        byte[] rowKeyPrefix = Bytes.toBytes(reversedUserId);

        FilterList filterList = new FilterList();
        filterList.addFilter(new PrefixFilter(rowKeyPrefix));  // 1. 用 rowKeyPrefix 来过滤出该用户的 pass
        filterList.addFilter(new SingleColumnValueFilter(      // 2. 用 template id 来过滤出该 passTemplate 下的 pass
            HBaseTable.PassTable.FAMILY_I.getBytes(),
            HBaseTable.PassTable.TEMPLATE_ID.getBytes(),
            CompareFilter.CompareOp.EQUAL,
            Bytes.toBytes(pass.getTemplateId())
        ));
        filterList.addFilter(new SingleColumnValueFilter(      // 3. 用 -1 来过滤出还未被消费的 pass
            HBaseTable.PassTable.FAMILY_I.getBytes(),
            HBaseTable.PassTable.CONSUME_DATE.getBytes(),
            CompareFilter.CompareOp.EQUAL,
            Bytes.toBytes("-1")
        ));

        Scan scan = new Scan();
        scan.setFilter(filterList);     // 由3个过滤器组装出的 scan（FilterList 中的 filter 之间默认是 AND 关系）

        List<Pass> passes = hbaseTemplate.find(HBaseTable.PassTable.TABLE_NAME, scan, new PassRowMapper());

        return passes == null || passes.size() != 1;      // 若 HBase 中找不到符合条件的 pass 则说明该 pass 无效
    }

    private List<PassInfo> getPassInfosByStatus(Long userId, PassStatus status) throws Exception {
        Scan scan = buildScanForUserAndStatus(userId, status);
        List<Pass> passes = hbaseTemplate.find(HBaseTable.PassTable.TABLE_NAME, scan, new PassRowMapper());
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        Map<Integer, Merchant> merchantsMap = buildMerchantMap(new ArrayList<>(passTemplateMap.values()));
        return buildPassInfoList(passes, passTemplateMap, merchantsMap);
    }

    private Scan buildScanForUserAndStatus(Long userId, PassStatus status) {
        String reversedUserId = new StringBuilder(String.valueOf(userId)).reverse().toString();
        byte[] rowPrefix = Bytes.toBytes(reversedUserId);  // 根据 userId 构造行键前缀

        FilterList filterList = new FilterList();
        filterList.addFilter(new PrefixFilter(rowPrefix));  // 1. 行键前缀过滤器，找到特定用户的优惠券

        if (status != PassStatus.ALL) {                     // 2. 若要查找的是已使用/未使用的优惠券，就再加一个基于列单元值的过滤器
            CompareFilter.CompareOp compareOp = (status == PassStatus.UNUSED)  // 根据要查找的状态选择比较器
                ? CompareFilter.CompareOp.EQUAL
                : CompareFilter.CompareOp.NOT_EQUAL;

            filterList.addFilter(new SingleColumnValueFilter(
                HBaseTable.PassTable.FAMILY_I.getBytes(),
                HBaseTable.PassTable.CONSUME_DATE.getBytes(),
                compareOp,
                Bytes.toBytes("-1"))  // 根据 consume_date 字段是否等于 -1 来判断优惠券状态，从而过滤优惠券
            );
        }

        Scan scan = new Scan();
        scan.setFilter(filterList);  // 多个过滤器之间默认是 AND 关系（如需要也可以设置而 OR 关系）
        return scan;
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

        return merchantClient.getMerchants(merchantsIds).stream()
            .collect(Collectors.toMap(Merchant::getId, merchant -> merchant));
    }

    // 生成 PassInfo 列表（包含 pass, passTemplate, merchant 信息）
    private List<PassInfo> buildPassInfoList(List<Pass> passes,
                                             Map<String, PassTemplate> passTemplateMap,
                                             Map<Integer, Merchant> merchantsMap) {
        List<PassInfo> passInfos = new ArrayList<>();

        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap  // 通过 pass 中的 template id 在 passTemplateMap 中找到 passTemplate 对象
                .getOrDefault(pass.getTemplateId(), null);

            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchant merchants = merchantsMap  // 通过 passTemplate 中的 id 在 merchantMap 中找到 merchant 对象
                .getOrDefault(passTemplate.getId(), null);

            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getId());
                continue;
            }

            passInfos.add(new PassInfo(pass, passTemplate, merchants));  // 三个信息最终合成 passInfo 对象
        }

        return passInfos;
    }
}
