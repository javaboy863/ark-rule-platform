package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.RuleTemplateMetaDao;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDOExample;
import com.ark.rule.platform.domain.dto.response.TemplateMetaBaseResDTO;
import com.ark.rule.platform.domain.service.db.ITemplateMetaDbService;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * 描述类的功能.
 *
 */
@Service("templateMetaService")
@Slf4j
public class TemplateMetaDbServiceImpl implements ITemplateMetaDbService {
    @Resource
    private RuleTemplateMetaDao ruleTemplateMetaDao;

    @Override
    public List<TemplateMetaBaseResDTO> getTemplateMetaByTid(Long templateId) {
        RuleTemplateMetaDOExample example = new RuleTemplateMetaDOExample();
        example.createCriteria().andTemplateIdEqualTo(templateId);
        List<RuleTemplateMetaDO> templateMetaDOList = ruleTemplateMetaDao.selectByExample(example);
        if (CollectionUtils.isEmpty(templateMetaDOList)) {
            return Lists.newArrayList();
        }
        return templateMetaDOList.stream()
                .map(ruleTemplateMetaDO -> BeanConvertUtil
                    .conver(ruleTemplateMetaDO, TemplateMetaBaseResDTO.class))
                .collect(Collectors.toList());
    }
}

