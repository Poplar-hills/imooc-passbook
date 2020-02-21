package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.PassTemplate;

public interface IHbasePassTemplateService {

    // 将 pass template 写入 HBase，返回成功或失败
    boolean savePassTemplateToHbase(PassTemplate passTemplate);
}
