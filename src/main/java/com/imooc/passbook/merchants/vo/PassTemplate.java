package com.imooc.passbook.merchants.vo;

import com.imooc.passbook.merchants.constants.ErrorCode;
import com.imooc.passbook.merchants.dao.MerchantDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 将优惠券信息进行封装成 Value Object，用于在业务和服务之间传递
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplate {

    // 所属商户 id  // TODO: 优惠券有所属商户 id，但自己没有 id???
    private Integer id;

    // 优惠券标题
    private String title;

    // 优惠券摘要
    private String summary;

    // 优惠券的详细信息
    private String desc;

    // 优惠券最大发放个数
    private Long limit;

    // 优惠券是否有 token 需要商户核销
    private Boolean hasToken;

    // 优惠券背景色
    private Integer background;

    // 优惠券开始时间
    private Date start;

    // 优惠券结束时间（消费时间）
    private Date end;

    // 校验优惠券的有效性
    public ErrorCode validate(MerchantDao merchantDao) {  // TODO: validate 方法放在 VO 类里而不是 service 里？？？
        return merchantDao.findById(id) == null            // TODO: validate 方法返回 ErrorCode？那校验成功怎么办？？？
            ? ErrorCode.MERCHANT_NOT_EXIST
            : ErrorCode.SUCCESS;
    }
}
