
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.RuleMetaDao;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDOExample;
import com.ark.rule.platform.domain.dto.response.RuleMetaBaseDTO;
import com.ark.rule.platform.domain.service.db.IRuleMetaDbService;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * 描述类的功能.
 *
 */
@Service("ruleMetaDbService")
public class RuleMetaDbServiceImpl implements IRuleMetaDbService {

    @Resource
    private RuleMetaDao ruleMetaDao;

    @Override
    public List<RuleMetaBaseDTO> queryRuleMetasByRuleIds(List<Long> ruleIds) {
        if (CollectionUtils.isEmpty(ruleIds)) {
            return Lists.newArrayList();
        }
        RuleMetaDOExample example = new RuleMetaDOExample();
        example.createCriteria().andRuleIdIn(ruleIds);
        List<RuleMetaDO> ruleMetaDOList = ruleMetaDao.selectByExample(example);
        if (CollectionUtils.isEmpty(ruleMetaDOList)) {
            return Lists.newArrayList();
        }

        return ruleMetaDOList.stream().map(ruleMetaDO -> BeanConvertUtil
            .conver(ruleMetaDO, RuleMetaBaseDTO.class))
                .collect(Collectors.toList());
    }
}

