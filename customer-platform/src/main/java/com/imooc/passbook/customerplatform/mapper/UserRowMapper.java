package com.imooc.passbook.customerplatform.mapper;

import com.imooc.passbook.customerplatform.constants.HBaseTable;
import com.imooc.passbook.customerplatform.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Mapper for mapping HBase User row to User object
 * - User 的 ORM 映射，即将 HBase User 表中的 Row 映射成 Java POJO。
 */

public class UserRowMapper implements RowMapper<User> {

    // ∵ HBase 中没有类型，key、value 都是以字节数组存储的 ∴ 需要在这里将需要用到的 key 转化为字节数组
    private static byte[] FAMILY_B = HBaseTable.UserTable.FAMILY_B.getBytes();
    private static byte[] NAME = HBaseTable.UserTable.NAME.getBytes();
    private static byte[] AGE = HBaseTable.UserTable.AGE.getBytes();
    private static byte[] SEX = HBaseTable.UserTable.SEX.getBytes();

    private static byte[] FAMILY_O = HBaseTable.UserTable.FAMILY_O.getBytes();
    private static byte[] PHONE = HBaseTable.UserTable.PHONE.getBytes();
    private static byte[] ADDRESS = HBaseTable.UserTable.ADDRESS.getBytes();

    @Override
    public User mapRow(Result result, int rowNum) throws Exception {  // result 是读取解析 Cell 数据，rowNum 是取出的行数
        User.BaseInfo baseInfo = new User.BaseInfo(
            Bytes.toString(result.getValue(FAMILY_B, NAME)),  // result.getValue() 通过 family 和 qualifier 从 HBase 中读取数据单元（Cell）
            Bytes.toInt(result.getValue(FAMILY_B, AGE)),
            Bytes.toString(result.getValue(FAMILY_B, SEX))
        );
        User.OtherInfo otherInfo = new User.OtherInfo(
            Bytes.toString(result.getValue(FAMILY_O, PHONE)),
            Bytes.toString(result.getValue(FAMILY_O, ADDRESS))
        );
        return User.builder()
            .id(Bytes.toLong(result.getRow()))  // 使用 User 表的 Row key 作为 User 对象的 id
            .baseInfo(baseInfo)
            .otherInfo(otherInfo)
            .build();
    }
}
