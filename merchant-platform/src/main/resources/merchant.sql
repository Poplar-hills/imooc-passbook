CREATE TABLE `merchant` (
    `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL COMMENT '商户名称',
    `logo_url` VARCHAR(256) NOT NULL COMMENT '商户 logo URL',
    `business_license_url` VARCHAR(256) NOT NULL COMMENT '商户营业执照 URL',
    `phone` VARCHAR(64) NOT NULL COMMENT '商户电话',
    `address` VARCHAR(64) NOT NULL COMMENT '商户地址',
    `is_audit` BOOLEAN NOT NULL COMMENT '是否通过审核',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARACTER SET=utf8;