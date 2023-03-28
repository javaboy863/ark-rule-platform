
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.RuleTemplateDao;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateDOExample;
import com.ark.rule.platform.domain.dto.inner.IRuleTemplateDTO;
import com.ark.rule.platform.domain.dto.response.RuleTemplateBaseResDTO;
import com.ark.rule.platform.domain.service.db.IRuleTemplateDbService;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则模板服务实现.
 *
 */
@Service("ruleTemplateService")
@Slf4j
public class RuleTemplateDbServiceImpl implements IRuleTemplateDbService {
    @Resource
    private RuleTemplateDao ruleTemplateDao;

    @Override
    public List<IRuleTemplateDTO> queryAllRuleTemplate() {
        RuleTemplateDOExample example = new RuleTemplateDOExample();
        example.createCriteria().andIsDeleteEqualTo(0);
        List<RuleTemplateDO> ruleTemplates = null;
        try {
            ruleTemplates = ruleTemplateDao.selectByExample(example);
        } catch (Exception e) {
            log.error("queryAllRuleTemplate error:", e);
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(ruleTemplates)) {
            return Lists.newArrayList();
        }
        List<IRuleTemplateDTO> ruleTemplateDTOList =
                ruleTemplates.stream().map(this::convertDOToDTO).collect(Collectors.toList());
        return ruleTemplateDTOList;
    }

    @Override
    public RuleTemplateBaseResDTO queryRuleTemplateBaseInfoById(Long templateId) {
        RuleTemplateDO ruleTemplateDO = ruleTemplateDao.selectByPrimaryKey(templateId);
        if (ruleTemplateDO == null) {
            return null;
        }
        return BeanConvertUtil.conver(ruleTemplateDO, RuleTemplateBaseResDTO.class);
    }

    /**
     * 将do转成Dto
     *
     * @param ruleTemplateDO ''
     * @return ''
     */
    private IRuleTemplateDTO convertDOToDTO(RuleTemplateDO ruleTemplateDO) {
        IRuleTemplateDTO ruleTemplateDTO = new IRuleTemplateDTO();
        BeanUtils.copyProperties(ruleTemplateDO, ruleTemplateDTO);
        return ruleTemplateDTO;
    }
}

