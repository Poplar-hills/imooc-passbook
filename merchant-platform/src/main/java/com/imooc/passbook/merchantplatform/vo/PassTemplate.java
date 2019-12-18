package com.imooc.passbook.merchantplatform.vo;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.dao.MerchantDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 将优惠券信息进行封装成 Value Object，用于在业务和服务之间传递
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassTemplate {

    // 所属商户 id  // TODO: 优惠券有所属商户 id，但自己没有 id？？？ 优惠劵不在商户 DB 里保持？？？
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
    public ErrorCode validate(MerchantDao merchantDao) {
        return !merchantDao.findById(id).isPresent()  // 通过 MerchantDao 去 DB 中对 id 进行校验
            ? ErrorCode.MERCHANT_NOT_EXIST
            : ErrorCode.SUCCESS;
    }
}
