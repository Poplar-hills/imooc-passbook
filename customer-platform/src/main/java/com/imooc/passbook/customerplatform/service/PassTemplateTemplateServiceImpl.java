package com.imooc.passbook.customerplatform.service;

import com.imooc.passbook.customerplatform.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 */

@Slf4j
@Service
public class PassTemplateTemplateServiceImpl implements IHbasePassTemplateService {

    @Override
    public boolean savePassTemplateToHbase(PassTemplate passTemplate) {
        return false;
    }
}
