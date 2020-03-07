package com.imooc.passbook.customerplatform.service.impl;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.entity.Merchant;
import com.imooc.passbook.customerplatform.external.MerchantClient;
import com.imooc.passbook.customerplatform.orm.PassTemplateRowMapper;
import com.imooc.passbook.customerplatform.service.IInventoryService;
import com.imooc.passbook.customerplatform.service.IUserPassService;
import com.imooc.passbook.customerplatform.utils.RowKeyGenerator;
import com.imooc.passbook.customerplatform.vo.AvailablePassTemplates;
import com.imooc.passbook.customerplatform.vo.PassInfo;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import com.imooc.passbook.customerplatform.vo.PassTemplateInfo;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.AllArgsConstructor;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final IUserPassService userPassService;
    private final HbaseTemplate hbaseTemplate;
    private final MerchantClient merchantClient;

    // 获取用户可领取的优惠券（ = 系统中可用的优惠券 - 用户已领取的优惠券）
    @Override
    public AvailablePassTemplates getAvailablePassTemplates(Long userId) throws Exception {
        List<String> idsToExclude = userPassService.getAllUserPassInfos(userId)  // 获取用户所有已领取的优惠券
            .stream()
            .map(PassInfo::getPassTemplate)
            .map(RowKeyGenerator::genForPassTemplate)  // 获取每个已领取优惠券的 row key（TODO: PassTemplate 应该有自己的 id，而不是每次用的要现去生成）
            .collect(Collectors.toList());

        List<PassTemplate> availablePassTemplates = getInStockPassTemplates()  // 获取系统中所有还有库存的优惠券
            .stream()
            .filter(passTemplate -> {                  // 过滤出当前时间可用的优惠券
                LocalDateTime now = LocalDateTime.now();
                return now.isAfter(passTemplate.getStartTime()) && now.isBefore(passTemplate.getEndTime());
            })
            .filter(passTemplate -> {                  // 过滤出用户还未领取的优惠券
                String id = RowKeyGenerator.genForPassTemplate(passTemplate);
                return !idsToExclude.contains(id);
            })
            .collect(Collectors.toList());

        List<PassTemplateInfo> availablePassTemplateInfos = generatePassTemplateInfos(availablePassTemplates);
        return new AvailablePassTemplates(userId, availablePassTemplateInfos);
    }

    private List<PassTemplate> getInStockPassTemplates() {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);  // 设置 filter 之间是 OR 的关系

        filterList.addFilter(new SingleColumnValueFilter(
            Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
            Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
            CompareFilter.CompareOp.GREATER,
            new LongComparator(0L)        // 过滤出 Limit > 0（即有库存）的优惠券
        ));
        filterList.addFilter(new SingleColumnValueFilter(
            Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
            Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
            CompareFilter.CompareOp.EQUAL,
            new LongComparator(-1)        // 过滤出 Limit == -1（即没有库存限制）的优惠券
        ));

        Scan scan = new Scan();
        scan.setFilter(filterList);

        return hbaseTemplate.find(
            HBaseTable.PassTemplateTable.TABLE_NAME, scan, new PassTemplateRowMapper());
    }

    private List<PassTemplateInfo> generatePassTemplateInfos(List<PassTemplate> passTemplates) {
        List<Integer> merchantIds = passTemplates.stream()  // 最后给每个 passTemplate 加上 merchant 信息，生成 PassTemplateInfo
            .map(PassTemplate::getId)
            .collect(Collectors.toList());

        Map<Integer, Merchant> merchants = merchantClient.getMerchants(merchantIds).stream()  // 从 merchant-platform 请求数据
            .collect(Collectors.toMap(Merchant::getId, merchant -> merchant));

        return passTemplates.stream()
            .map(passTemplate -> {
                Merchant merchant = merchants.get(passTemplate.getId());
                return new PassTemplateInfo(passTemplate, merchant);
            })
            .collect(Collectors.toList());
    }
}
