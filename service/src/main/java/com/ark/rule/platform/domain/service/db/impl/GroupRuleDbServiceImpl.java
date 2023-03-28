
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.GroupRuleDao;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDO;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDOExample;
import com.ark.rule.platform.domain.dto.response.GroupRuleBaseResDTO;
import com.ark.rule.platform.domain.enums.DataValueEnum;
import com.ark.rule.platform.domain.service.db.IGroupRuleDbService;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述类的功能.
 *
 */
@Service("groupRuleService")
@Slf4j
public class GroupRuleDbServiceImpl implements IGroupRuleDbService {
    @Resource
    private GroupRuleDao groupRuleDao;

    @Override
    public GroupRuleBaseResDTO queryGroupRuleBaseInfo(Long groupId) {
        GroupRuleDO groupRuleDO = groupRuleDao.selectByPrimaryKey(groupId);
        if (groupRuleDO == null) {
            return null;
        }
        return BeanConvertUtil.conver(groupRuleDO, GroupRuleBaseResDTO.class);
    }

    @Override
    public List<GroupRuleBaseResDTO> queryAllValidGroup() {
        GroupRuleDOExample example = new GroupRuleDOExample();
        example.createCriteria().andIsDeleteEqualTo(DataValueEnum.NO_DELETA.getCode());
        List<GroupRuleDO> groupRuleDOS = groupRuleDao.selectByExample(example);
        if (CollectionUtils.isEmpty(groupRuleDOS)) {
            return Lists.newArrayList();
        }
        return groupRuleDOS.stream()
                .map(groupRuleDO -> BeanConvertUtil.conver(groupRuleDO, GroupRuleBaseResDTO.class))
                .collect(Collectors.toList());
    }
}

