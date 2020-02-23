package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.utils.RowKeyGenerator;
import com.imooc.passbook.customerplatform.vo.Response;
import com.imooc.passbook.customerplatform.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

/**
 * 用于在 HBase 中读写 User 的 service
 */

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final HbaseTemplate hbaseTemplate;  // HBase 客户端

    private final StringRedisTemplate redisTemplate;  // Redis 客户端 TODO: vs. Jedis?????

    public UserServiceImpl(HbaseTemplate hbaseTemplate, StringRedisTemplate redisTemplate) {
        this.hbaseTemplate = hbaseTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Response<User> createUser(User user) throws Exception {
        byte[] FAMILY_B = HBaseTable.UserTable.FAMILY_B.getBytes();
        byte[] NAME = HBaseTable.UserTable.NAME.getBytes();
        byte[] AGE = HBaseTable.UserTable.AGE.getBytes();
        byte[] SEX = HBaseTable.UserTable.SEX.getBytes();

        byte[] FAMILY_O = HBaseTable.UserTable.FAMILY_O.getBytes();
        byte[] PHONE = HBaseTable.UserTable.PHONE.getBytes();
        byte[] ADDRESS = HBaseTable.UserTable.ADDRESS.getBytes();

        Long currUserNum = redisTemplate.opsForV

        Long userId = genUserId(1L);
        return null;
    }

    private Long genUserId(Long prefix) {
        String suffix = RandomStringUtils.randomNumeric(5);
        return Long.valueOf(prefix + suffix);
    }
}
















