package com.imooc.passbook.customerplatform.utils;

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
     * - 这里的 Row key 之所以要用 MD5 加密是因为：首先 HBase 通常是集群，其中数据都是基于 row key 存储，且 row key 相近的值会被
     *   存储在一起 ∴ 若不将 row key 处理的分散一点，则会让数据存储在一个节点上，不利于负载均衡。反过来就是 row key 约分散，数据在
     *   HBase 中存储地就越分散到多个节点上，使得不会出现单节点性能瓶颈。
     */
    public static String genForPassTemplate(PassTemplate passTemplate) {
        String passTemplateStr = String.valueOf(passTemplate.getId()) + "_" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passTemplateStr);
        log.info("genForPassTemplate: {}, {}", passTemplate, rowKey);
        return rowKey;
    }

    /**
     * 根据 Feedback 的 id、title 生成 Row key。
     * 1. 基于 userId 生成 row key，这是因为让 row key 中包含 user id 信息会便于查找。
     * 2. ∵ 系统有个功能是用户浏览自己所有的 feedback ∴ 让同一用户的 feedback 存储在相近的位置是最好的，便于 scan 其所有 feedback。
     * 3. 之所以要将 userId reverse 一下是因为 userId 的前缀就是系统用户数，当用户数很大时，前缀都会是相同的，而后缀是分散的 ∴ 将其
     *    reverse 使得用户数据能更分散的保存在集群的各个节点上。
     * 4. row key 的后缀 postfix 是由最大值 - 当前系统实现 ∵ 生成 feedback row key 的时候实际上就是创建 feedback 的时间，这时取
     *    当前时间戳则新创建的 feedback 的时间戳要比之前创建的 feedback 时间戳大，而用最大值一减就反过来了 —— 新 feedback 的时间戳
     *    要比旧 feedback 的时间戳小，这样在 scan 的时候直接就能取得按时间倒序排好序的 feedback 列表。
     */
    public static String genForFeedback(Feedback feedback) {
        String userIdStr = String.valueOf(feedback.getUserId());
        String reversedUserIdStr = new StringBuilder(userIdStr).reverse().toString();
        long postfix = Long.MAX_VALUE - System.currentTimeMillis();
        String rowKey = reversedUserIdStr + postfix;
        log.info("genForFeedback: {}", rowKey);
        return rowKey;
    }
}
