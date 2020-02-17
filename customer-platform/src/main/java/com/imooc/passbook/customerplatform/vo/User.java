package com.imooc.passbook.customerplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User 对象定义
 * - baseInfo 和 otherInfo 其实就是 HBase 中的两个列族的定义
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private BaseInfo baseInfo;  // 用户基本信息

    private OtherInfo otherInfo;  // 用户额外信息

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseInfo {

        private String name;
        private Integer age;
        private String sex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherInfo {

        private String phone;
        private String address;
    }
}
