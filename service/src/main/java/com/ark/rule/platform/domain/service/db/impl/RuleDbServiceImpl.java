
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.RuleDao;
import com.ark.rule.platform.domain.dao.domain.RuleDO;
import com.ark.rule.platform.domain.dao.domain.RuleDOExample;
import com.ark.rule.platform.domain.dto.response.RuleBaseDTO;
import com.ark.rule.platform.domain.enums.DataValueEnum;
import com.ark.rule.platform.domain.service.db.IRuleDbService;
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
@Service("ruleDbService")
public class RuleDbServiceImpl implements IRuleDbService {

    @Resource
    private RuleDao ruleDao;

    @Override
    public List<RuleBaseDTO> queryRulesByGroupId(Long groupId) {
        RuleDOExample example = new RuleDOExample();
        example.createCriteria().andGroupIdEqualTo(groupId).andIsDeleteEqualTo(DataValueEnum.NO_DELETA.getCode());
        List<RuleDO> ruleList = ruleDao.selectByExample(example);
        if (CollectionUtils.isEmpty(ruleList)) {
            return Lists.newArrayList();
        }
        return ruleList.stream().map(ruleDO -> BeanConvertUtil.conver(ruleDO, RuleBaseDTO.class))
                .collect(Collectors.toList());
    }
}

