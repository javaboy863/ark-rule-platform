
package com.ark.rule.platform.domain.service.bg.impl;

import com.ark.rule.platform.domain.dao.GroupRuleDao;
import com.ark.rule.platform.domain.dao.RuleDao;
import com.ark.rule.platform.domain.dao.RuleMetaDao;
import com.ark.rule.platform.domain.dao.RuleResultDao;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDO;
import com.ark.rule.platform.domain.dao.domain.RuleDO;
import com.ark.rule.platform.domain.dao.domain.RuleDOExample;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleResultDO;
import com.ark.rule.platform.domain.dto.req.AddRuleGroupReqDTO;
import com.ark.rule.platform.domain.dto.req.DelGroupRuleReqDTO;
import com.ark.rule.platform.domain.dto.req.RuleMetaReqDTO;
import com.ark.rule.platform.domain.dto.req.RuleReqDTO;
import com.ark.rule.platform.domain.dto.req.UpdateRuleGroupReqDTO;
import com.ark.rule.platform.domain.enums.DataValueEnum;
import com.ark.rule.platform.domain.enums.ResultEnum;
import com.ark.rule.platform.domain.exception.TransException;
import com.ark.rule.platform.domain.service.bg.RuleBgFacadeService;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 规则facade层接口实现.
 *
 */
@Service("ruleBgFacadeService")
public class RuleBgFacadeServiceImpl implements RuleBgFacadeService {
    @Resource
    private GroupRuleDao groupRuleDao;

    @Resource
    private RuleDao ruleDao;

    @Resource
    private RuleMetaDao ruleMetaDao;

    @Resource
    private RuleResultDao ruleResultDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRuleGroup(AddRuleGroupReqDTO ruleGroup) {
        //新增规则组数据
        Long groupId = addGroupRuleConfig(ruleGroup);
        if (CollectionUtils.isEmpty(ruleGroup.getRules())) {
            return groupId;
        }
        //新增规则数据
        addRuleListConfig(groupId, ruleGroup.getRules());
        return groupId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRuleGroup(UpdateRuleGroupReqDTO groupRule) {
        Long groupId = groupRule.getGroupId();
        //更新group
        updateGroup(groupId, groupRule.getOperator());
        //删除原规则信息
        delRuleByGroupId(groupId, groupRule.getOperator());
        if (CollectionUtils.isEmpty(groupRule.getRules())) {
            return;
        }
        //新增规则数据
        addRuleListConfig(groupId, groupRule.getRules());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroupRule(DelGroupRuleReqDTO delGroupRuleReqDTO) {
        //删除group_rule
        GroupRuleDO groupRuleDO = new GroupRuleDO();
        groupRuleDO.setId(delGroupRuleReqDTO.getGroupId());
        groupRuleDO.setUpdateUser(delGroupRuleReqDTO.getOperator());
        groupRuleDO.setIsDelete(DataValueEnum.DELETA.getCode());
        groupRuleDao.updateByPrimaryKeySelective(groupRuleDO);

        //删除规则组下所有规则
        delRuleByGroupId(delGroupRuleReqDTO.getGroupId(), delGroupRuleReqDTO.getOperator());
    }

    /**
     * 新增规则组配置信息.
     *
     * @param ruleGroup ''
     * @return groupId
     */
    private Long addGroupRuleConfig(AddRuleGroupReqDTO ruleGroup) {
        GroupRuleDO groupRuleDO = gainGroupRuleDO(ruleGroup);
        groupRuleDao.insertSelective(groupRuleDO);
        return groupRuleDO.getId();
    }

    /**
     * 组装规则组数据.
     *
     * @param ruleGroup ''
     * @return ''
     */
    private GroupRuleDO gainGroupRuleDO(AddRuleGroupReqDTO ruleGroup) {
        GroupRuleDO groupRuleDO = new GroupRuleDO();
        groupRuleDO.setGroupName(ruleGroup.getName());
        groupRuleDO.setBizCode(ruleGroup.getBizCode());
        groupRuleDO.setTemplateId(ruleGroup.getTemplateId());
        groupRuleDO.setScript(ruleGroup.getScript());
        groupRuleDO.setIsDelete(DataValueEnum.NO_DELETA.getCode());
        groupRuleDO.setCreateUser(ruleGroup.getCreateUser());
        groupRuleDO.setUpdateUser(ruleGroup.getCreateUser());
        return groupRuleDO;
    }

    /**
     * 规则列表新增.
     *
     * @param groupId 规则组id
     * @param rules   规则信息.
     */
    private void addRuleListConfig(Long groupId, List<RuleReqDTO> rules) {
        for (RuleReqDTO rule : rules) {
            addRuleConfig(rule, groupId);
        }
    }

    /**
     * 新增规则配置.
     *
     * @param rule    规则
     * @param groupId 规则组id
     * @return ''
     */
    private void addRuleConfig(RuleReqDTO rule, Long groupId) {
        RuleDO ruleDO = gainAddRuleDO(rule, groupId);
        ruleDao.insertSelective(ruleDO);
        addRuleMeta(ruleDO, rule.getMetas());
        addRuleResult(ruleDO.getId(), rule);
    }

    /**
     * 新增规则结果.
     *
     * @param ruleId 规则id
     * @param rule   规则信息
     */
    private void addRuleResult(Long ruleId, RuleReqDTO rule) {
        RuleResultDO ruleResultDO = new RuleResultDO();
        ruleResultDO.setRuleId(ruleId);
        ruleResultDO.setResult(rule.getResult());
        ruleResultDO.setCreateUser(rule.getOperator());
        ruleResultDO.setUpdateUser(rule.getOperator());

        ruleResultDao.insertSelective(ruleResultDO);
    }

    /**
     * 组装新增规则数据do.
     *
     * @param rule    规则数据
     * @param groupId 规则组id
     * @return ''
     */
    private RuleDO gainAddRuleDO(RuleReqDTO rule, Long groupId) {
        RuleDO ruleDO = new RuleDO();
        ruleDO.setRuleName(rule.getName());
        ruleDO.setPriority(rule.getPriority());
        ruleDO.setGroupId(groupId);
        ruleDO.setIsDelete(DataValueEnum.NO_DELETA.getCode());
        ruleDO.setCreateUser(rule.getOperator());
        ruleDO.setUpdateUser(rule.getOperator());
        return ruleDO;
    }

    /**
     * 新增规则元数据.
     *
     * @param rule  规则
     * @param metas 元数据
     */
    private void addRuleMeta(RuleDO rule, List<RuleMetaReqDTO> metas) {
        List<RuleMetaDO> ruleMetaDOList = gainRuleMetaDOList(rule, metas);
        if (CollectionUtils.isEmpty(ruleMetaDOList)) {
            return;
        }
        ruleMetaDao.batchInsert(ruleMetaDOList);
    }

    /**
     * 组装元数据列表.
     *
     * @param rule  规则id
     * @param metas 规则元数据配置数据
     * @return ''
     */
    private List<RuleMetaDO> gainRuleMetaDOList(RuleDO rule, List<RuleMetaReqDTO> metas) {
        List<RuleMetaDO> ruleMetaDOList = Lists.newArrayList();
        for (RuleMetaReqDTO metaReqDTO : metas) {
            if (metaReqDTO == null) {
                continue;
            }
            RuleMetaDO ruleMetaDO = gainRuleMetaDO(metaReqDTO, rule);
            if (ruleMetaDO == null) {
                continue;
            }
            ruleMetaDOList.add(ruleMetaDO);
        }
        return ruleMetaDOList;
    }

    /**
     * 组装规则元数据do对象
     *
     * @param metaReqDTO 规则元数据
     * @param rule       规则
     * @return ''
     */
    private RuleMetaDO gainRuleMetaDO(RuleMetaReqDTO metaReqDTO, RuleDO rule) {
        RuleMetaDO ruleMetaDO = new RuleMetaDO();
        ruleMetaDO.setRuleId(rule.getId());
        ruleMetaDO.setMetaCode(metaReqDTO.getCode());
        ruleMetaDO.setMetaValue(metaReqDTO.getValue());
        ruleMetaDO.setCreateUser(rule.getUpdateUser());
        ruleMetaDO.setUpdateUser(rule.getUpdateUser());
        return ruleMetaDO;
    }

    /**
     * 删除规则组原有规则.
     *
     * @param groupId  规则组id
     * @param operator 操作人
     */
    private void delRuleByGroupId(Long groupId, String operator) {
        RuleDO ruleDO = new RuleDO();
        ruleDO.setUpdateUser(operator);
        ruleDO.setIsDelete(DataValueEnum.DELETA.getCode());

        RuleDOExample example = new RuleDOExample();
        //已删除无须再次删除
        example.createCriteria().andGroupIdEqualTo(groupId).andIsDeleteEqualTo(DataValueEnum.NO_DELETA.getCode());
        ruleDao.updateByExampleSelective(ruleDO, example);
    }

    /**
     * 更新规则组信息。
     *
     * @param groupId  规则组id
     * @param operator 操作人
     */
    private void updateGroup(Long groupId, String operator) {
        GroupRuleDO groupRuleDO = new GroupRuleDO();
        groupRuleDO.setId(groupId);
        groupRuleDO.setUpdateUser(operator);
        int count = groupRuleDao.updateByPrimaryKeySelective(groupRuleDO);
        if (count <= 0) {
            throw new TransException(ResultEnum.SYSTEM_ERROR, "更新规则组失败");
        }
    }

}

