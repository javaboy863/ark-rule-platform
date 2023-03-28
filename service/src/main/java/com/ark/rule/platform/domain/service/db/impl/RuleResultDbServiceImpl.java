
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.RuleResultDao;
import com.ark.rule.platform.domain.dao.domain.RuleResultDO;
import com.ark.rule.platform.domain.dao.domain.RuleResultDOExample;
import com.ark.rule.platform.domain.dto.response.RuleResultBaseDTO;
import com.ark.rule.platform.domain.service.db.IRuleResultDbService;
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
@Service("ruleResultDbService")
public class RuleResultDbServiceImpl implements IRuleResultDbService {
    @Resource
    private RuleResultDao ruleResultDao;

    @Override
    public List<RuleResultBaseDTO> queryRuleResultByRuleIds(List<Long> ruleIds) {
        if (CollectionUtils.isEmpty(ruleIds)) {
            return Lists.newArrayList();
        }
        RuleResultDOExample example = new RuleResultDOExample();
        example.createCriteria().andRuleIdIn(ruleIds);
        List<RuleResultDO> ruleResultDOS = ruleResultDao.selectByExample(example);
        if (CollectionUtils.isEmpty(ruleResultDOS)) {
            return Lists.newArrayList();
        }
        return ruleResultDOS.stream().map(ruleResultDO -> BeanConvertUtil
            .conver(ruleResultDO, RuleResultBaseDTO.class))
                .collect(Collectors.toList());

    }
}

