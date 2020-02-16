package com.imooc.passbook.customerplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)  // unique=true 指定该字段需要全局唯一
    private String name;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "business_license_url", nullable = false)
    private String businessLicenseUrl;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Builder.Default  // ∵ @Builder annotation doesn't populate values ∴ 需要加上这个注解来给 builder 设置默认值
    @Column(name = "is_audit", nullable = false)
    private Boolean isAudit = false;  // 默认值为 false

}
