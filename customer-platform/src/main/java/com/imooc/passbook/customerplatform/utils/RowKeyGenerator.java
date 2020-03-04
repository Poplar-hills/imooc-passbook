package com.imooc.passbook.customerplatform.utils;

import com.imooc.passbook.customerplatform.vo.CollectPassTemplateRequest;
import com.imooc.passbook.customerplatform.vo.Feedback;
import com.imooc.passbook.customerplatform.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * HBase Row key 生成器
 *
 * - 这里包含了很多设计 row key 的 tricks，但都是很常用的设计方式，能有效提高系统性能（在网上可以查到更多）。
 */

@Slf4j
public class RowKeyGenerator {

    /**
     * 根据 PassTemplate 的 id、title 生成 Row key。
     * - PassTemplate RowKey = passTemplate id + passTemplate title
     * - 这里的 Row key 之所以要用 MD5 加密是因为：首先 HBase 通常是集群，其中数据都是基于 row key 存储，且 row key 相近的值会被
     *   存储在一起 ∴ 若不将 row key 处理的分散一点，则会让数据存储在一个节点上，不利于负载均衡。反过来就是 row key 约分散，数据在
     *   HBase 中存储地就越分散到多个节点上，使得不会出现单节点性能瓶颈。
     */
    public static String genForPassTemplate(PassTemplate passTemplate) {
        String passTemplateStr = passTemplate.getId() + "_" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passTemplateStr);
        log.info("genForPassTemplate: {}, {}", passTemplate, rowKey);
        return rowKey;
    }

    /**
     * 根据 Feedback 的 id、title 生成 Row key。
     * - Feedback RowKey = reversed(userId) + inverse(timestamp)
     * 1. 基于 userId 生成 row key 是为了让 row key 中包含 user id 信息，便于查找。
     * 2. ∵ 用户可以浏览自己所有的 feedback ∴ 将同一用户的 feedback 存储在相近的位置更有利于高效查询（scan 同一用户的所有 feedback）。
     * 3. 之所以要将 userId reverse 一下是因为 userId 的前缀就是系统用户数，当用户数很大时，前缀都会是相同的，而后缀是分散的 ∴ 将其
     *    reverse 后会使得用户数据能更分散的保存在集群的各个节点上。
     * 4. row key 的后缀是由最大值 - 当前系统时间 ∵ 生成 feedback row key 的时间就是创建这条 feedback 的时间，这时取当前时间戳则新
     *    创建的 feedback 的时间戳 > 旧的 feedback 时间戳，而用最大值一减就反过来了，即新 feedback 的时间戳 < 旧 feedback 时间戳。
     *    ∵ HBase 对 row key 是按字典序排序，即时间戳小的记录会排在前面 ∴ 以这样规则生成的 row key 在 scan 查询时就实现了从新
     *    到旧的排序。
     */
    public static String genForFeedback(Feedback feedback) {
        String userIdStr = String.valueOf(feedback.getUserId());
        String rowKey = new StringBuilder(userIdStr).reverse().toString()
            + (Long.MAX_VALUE - System.currentTimeMillis());
        log.info("genForFeedback: {}", rowKey);
        return rowKey;
    }

    /**
     * 根据领取优惠券请求生成 RowKey（只可以在领取优惠券的时候使用）。
     * - Pass RowKey = reversed(userId) + inverse(timestamp) + PassTemplate RowKey
     * - 之所以要这么生成，理由与上面的 feedback row key 一样：
     *   1. reversed(userId) 是为了让数据更均匀的存储在集群的不同几点上；
     *   2. inverse(timestamp) 是为了让 HBase 在 scan 查询用户的所有 pass 时能直接得到按时间倒序排列的 pass 列表。
     *   3. PassTemplate RowKey 是为了将 passTemplate 的信息保存在 pass row key 中，方便以后查询（例如使用 string 包含型的过滤器
     *      来 scan 某个 passTemplate row key，来查看用户是否已领取过该优惠券）。
     */
    public static String genForPass(CollectPassTemplateRequest request) {
        String rowKey = new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()
            + (Long.MAX_VALUE - System.currentTimeMillis())
            + genForPassTemplate(request.getPassTemplate());  // passTemplate row key 的生成是幂等的过程
        log.info("genForPass: {}", rowKey);
        return rowKey;
    }
}
