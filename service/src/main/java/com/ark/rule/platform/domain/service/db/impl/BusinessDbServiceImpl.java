
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.BusinessDao;
import com.ark.rule.platform.domain.dao.domain.BusinessDO;
import com.ark.rule.platform.domain.dao.domain.BusinessDOExample;
import com.ark.rule.platform.domain.dto.response.BusinessBaseResDTO;
import com.ark.rule.platform.domain.service.db.IBusinessDbService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述类的功能.
 *
 */
@Service("businessService")
@Slf4j
public class BusinessDbServiceImpl implements IBusinessDbService {
    @Resource
    private BusinessDao businessDao;

    @Override
    public BusinessBaseResDTO queryBusinessByCode(String bizCode) {
        BusinessDOExample example = new BusinessDOExample();
        example.createCriteria().andBizCodeEqualTo(bizCode);
        List<BusinessDO> businessDOS = businessDao.selectByExample(example);
        if (CollectionUtils.isEmpty(businessDOS)) {
            return null;
        }
        BusinessDO businessDO = businessDOS.get(0);

        return BeanConvertUtil.conver(businessDO, BusinessBaseResDTO.class);
    }
}

