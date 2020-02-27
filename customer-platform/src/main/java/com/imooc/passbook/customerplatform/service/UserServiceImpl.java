package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.constants.Constants;
import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用于在 HBase 中读写 User 的 service
 */

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final HbaseTemplate hbaseTemplate;  // HBase 客户端

    private final StringRedisTemplate redisTemplate;  // Redis 客户端 TODO: vs. Jedis?????

    @Override
    public User createUser(User user) throws Exception {
        byte[] FAMILY_B = HBaseTable.UserTable.FAMILY_B.getBytes();
        byte[] NAME = HBaseTable.UserTable.NAME.getBytes();
        byte[] AGE = HBaseTable.UserTable.AGE.getBytes();
        byte[] SEX = HBaseTable.UserTable.SEX.getBytes();

        byte[] FAMILY_O = HBaseTable.UserTable.FAMILY_O.getBytes();
        byte[] PHONE = HBaseTable.UserTable.PHONE.getBytes();
        byte[] ADDRESS = HBaseTable.UserTable.ADDRESS.getBytes();

        Long currUserCount = redisTemplate.opsForValue()      // 从 Redis 中取出当前系统的用户数（opsForValue 返回所有 String 操作）
            .increment(Constants.USER_COUNT_REDIS_KEY, 1);  // 在已有用户总数上 +1 就是创建这个用户之后的用户总数
        Long userId = genUserId(currUserCount);

        Put put = new Put(Bytes.toBytes(userId));             // 开始组装 HBase put query
        put.addColumn(FAMILY_B, NAME, Bytes.toBytes(user.getBaseInfo().getName()))
            .addColumn(FAMILY_B, AGE, Bytes.toBytes(user.getBaseInfo().getAge()))
            .addColumn(FAMILY_B, SEX, Bytes.toBytes(user.getBaseInfo().getSex()))
            .addColumn(FAMILY_O, PHONE, Bytes.toBytes(user.getOtherInfo().getPhone()))
            .addColumn(FAMILY_O, ADDRESS, Bytes.toBytes(user.getOtherInfo().getAddress()));

        hbaseTemplate.saveOrUpdate(HBaseTable.UserTable.TABLE_NAME, put);  // 将 user 存入 HBase

        user.setId(userId);  // ∵ 传进来的 user 是不带 id 的 ∴ 给其设置 id 后返回
        return user;
    }

    private Long genUserId(Long prefix) {
        String suffix = RandomStringUtils.randomNumeric(5);
        return Long.valueOf(prefix + suffix);
    }
}
















