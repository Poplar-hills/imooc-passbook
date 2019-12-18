package com.imooc.passbook.merchantplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 指定生成的是 id type（若不指定则会在 DB 中寻找 hibernate_sequence 表而报错）
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

    @Column(name = "is_audit", nullable = false)
    private Boolean isAudit = false;  // 默认值为 false
}
