
package com.ark.rule.platform.biz.bg;

import com.ark.rule.platform.api.domain.bg.PageListDTO;
import com.ark.rule.platform.api.domain.bg.QueryParamDTO;
import com.ark.rule.platform.api.domain.bg.RuleTemplateDTO;
import com.ark.rule.platform.api.domain.bg.RuleTemplateMetaDTO;
import com.ark.rule.platform.api.service.bg.ITemplateBgService;
import com.ark.rule.platform.common.enums.OperatorEnum;
import com.ark.rule.platform.domain.dao.RuleTemplateDao;
import com.ark.rule.platform.domain.dao.RuleTemplateMetaDao;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateDOExample;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDOExample;
import com.ark.rule.platform.domain.dto.inner.IBusinessDTO;
import com.ark.rule.platform.domain.service.guava.IBusinessGuavaService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 模板相关服务.
 *
 */
@Slf4j
@Service("templateBgService")
public class TemplateBgServiceImpl implements ITemplateBgService {

    @Resource
    private RuleTemplateDao ruleTemplateDao;
    @Resource
    private RuleTemplateMetaDao ruleTemplateMetaDao;
    @Resource
    private IBusinessGuavaService businessGuavaService;

    @Override
    public boolean isUsed(String bizCode) {
        if (StringUtils.isBlank(bizCode)) {
            throw new IllegalArgumentException("bizCode can not be null");
        }
        RuleTemplateDOExample example = new RuleTemplateDOExample();
        example.createCriteria().andBizCodeEqualTo(bizCode).andIsDeleteEqualTo(0);
        example.setLimit(1);
        List<RuleTemplateDO> dataList = ruleTemplateDao.selectByExample(example);
        log.info("dataList = {}", dataList);
        return !CollectionUtils.isEmpty(dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(RuleTemplateDTO dto, String user) {
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("dto can not be null");
        }

        long templateId = addTemplate(dto, user);
        log.info("templateId = {}", templateId);
        addTemplateMeta(dto.getMetaList(), user, templateId);

        return templateId;
    }

    private void addTemplateMeta(List<RuleTemplateMetaDTO> metaList, String user, long templateId) {
        RuleTemplateMetaDO temp;
        for (RuleTemplateMetaDTO item : metaList) {
            temp = new RuleTemplateMetaDO();
            temp.setTemplateId(templateId);
            temp.setMetaCode(item.getMetaCode());
            temp.setOperator(item.getOperator());
            temp.setCreateTime(new Date());
            temp.setUpdateTime(new Date());
            temp.setCreateUser(user);
            temp.setUpdateUser(user);
            ruleTemplateMetaDao.insert(temp);
        }
    }

    private long addTemplate(RuleTemplateDTO dto, String user) {
        List<RuleTemplateMetaDTO> metaList = dto.getMetaList();
        RuleTemplateDO ruleTemplateDO = new RuleTemplateDO();
        ruleTemplateDO.setBizCode(dto.getBizCode());
        ruleTemplateDO.setTemplateName(dto.getTemplateName());
        ruleTemplateDO.setScript(editScript(metaList));
        ruleTemplateDO.setIsDelete(0);
        ruleTemplateDO.setCreateTime(new Date());
        ruleTemplateDO.setUpdateTime(new Date());
        ruleTemplateDO.setCreateUser(user);
        ruleTemplateDO.setUpdateUser(user);
        ruleTemplateDao.insert(ruleTemplateDO);
        return ruleTemplateDO.getId();
    }

    private String editScript(List<RuleTemplateMetaDTO> metaList) {
        List<String> realFunctionList = new ArrayList<>();
        for (RuleTemplateMetaDTO item : metaList) {
            String metaCode = item.getMetaCode();
            String operator = item.getOperator();
            String functionName = OperatorEnum.getFunctionByOperator(operator);
            if (StringUtils.isNotBlank(functionName)) {
                String realFunction = functionName.replace("metaCode", metaCode);
                realFunctionList.add(realFunction);
            }
        }
        return StringUtils.join(realFunctionList, "&&");
    }

    @Override
    public Long delete(Long id, String user) {
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id 非法 :" + id);
        }
        RuleTemplateDO ruleTemplateDO = ruleTemplateDao.selectByPrimaryKey(id);
        log.info("ruleTemplateDO = {}", ruleTemplateDO);
        if (ruleTemplateDO == null || ruleTemplateDO.getIsDelete() == 1) {
            throw new IllegalArgumentException("data not exists or delete :" + id);
        }
        ruleTemplateDO.setIsDelete(1);
        ruleTemplateDO.setUpdateTime(new Date());
        ruleTemplateDO.setUpdateUser(user);
        ruleTemplateDao.updateByPrimaryKey(ruleTemplateDO);
        return id;
    }

    @Override
    public PageListDTO<RuleTemplateDTO> listByPage(QueryParamDTO query) {
        query.check();
        RuleTemplateDOExample example = editQueryExample(query);
        long num = ruleTemplateDao.countByExample(example);
        log.info("num = {}", num);
        if (num <= 0) {
            return this.defaultData();
        }
        List<RuleTemplateDO> ruleTemplateDOS = ruleTemplateDao.selectByExample(example);
        log.info("ruleTemplateDOS = {}", ruleTemplateDOS);
        if (CollectionUtils.isEmpty(ruleTemplateDOS)) {
            return this.defaultData();
        }
        PageListDTO<RuleTemplateDTO> result = editListResult(ruleTemplateDOS, num);
        return result;
    }

    private PageListDTO<RuleTemplateDTO> editListResult(List<RuleTemplateDO> ruleTemplateDOS, long num) {
        List<RuleTemplateDTO> dataList = new ArrayList<>();
        PageListDTO<RuleTemplateDTO> result = new PageListDTO<>();
        result.setTotalSize((int) num);
        result.setList(dataList);
        RuleTemplateDTO temp;
        for (RuleTemplateDO item : ruleTemplateDOS) {
            temp = new RuleTemplateDTO();
            BeanUtils.copyProperties(item, temp);
            dataList.add(temp);
            IBusinessDTO business = businessGuavaService.getBusinessByBizCode(item.getBizCode());
            if (business != null) {
                temp.setBizName(business.getBizName());
            }
        }
        return result;
    }

    private RuleTemplateDOExample editQueryExample(QueryParamDTO query) {
        RuleTemplateDOExample example = new RuleTemplateDOExample();
        RuleTemplateDOExample.Criteria criteria = example.createCriteria().andIsDeleteEqualTo(0);
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andTemplateNameLike("%" + query.getName() + "%");
        }
        if (StringUtils.isNotBlank(query.getCode())) {
            criteria.andBizCodeLike("%" + query.getCode() + "%");
        }
        example.setLimit(query.getSize());
        example.setOffset(query.getSize() * (query.getIndex() - 1));
        example.setOrderByClause("update_time desc");
        return example;
    }

    @Override
    public RuleTemplateDTO getById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id 非法 :" + id);
        }
        RuleTemplateDO ruleTemplateDO = ruleTemplateDao.selectByPrimaryKey(id);
        log.info("ruleTemplateDO = {}", ruleTemplateDO);
        if (ruleTemplateDO == null || ruleTemplateDO.getIsDelete() == 1) {
            throw new IllegalArgumentException("data not exists or delete :" + id);
        }
        RuleTemplateDTO result = new RuleTemplateDTO();
        BeanUtils.copyProperties(ruleTemplateDO, result);
        RuleTemplateMetaDOExample example = new RuleTemplateMetaDOExample();
        example.createCriteria().andTemplateIdEqualTo(ruleTemplateDO.getId());
        List<RuleTemplateMetaDO> ruleTemplateMetaDOS = ruleTemplateMetaDao.selectByExample(example);
        List<RuleTemplateMetaDTO> metaList = new ArrayList<>();
        if (ruleTemplateMetaDOS != null) {
            RuleTemplateMetaDTO temp;
            for (RuleTemplateMetaDO item : ruleTemplateMetaDOS) {
                temp = new RuleTemplateMetaDTO();
                metaList.add(temp);
                BeanUtils.copyProperties(item, temp);
            }
        }
        result.setMetaList(metaList);
        return result;
    }

    @Override
    public boolean isUserMeta(String metaCode) {
        if (StringUtils.isBlank(metaCode)) {
            return false;
        }

        RuleTemplateMetaDOExample example = new RuleTemplateMetaDOExample();
        example.createCriteria().andMetaCodeEqualTo(metaCode);
        List<RuleTemplateMetaDO> ruleTemplateMetaDOS = ruleTemplateMetaDao.selectByExample(example);
        log.info("ruleTemplateMetaDOS = {}", ruleTemplateMetaDOS);

        if (CollectionUtils.isEmpty(ruleTemplateMetaDOS)) {
            return false;
        }

        List<Long> templateIdList = new ArrayList<>();
        for (RuleTemplateMetaDO item : ruleTemplateMetaDOS) {
            templateIdList.add(item.getTemplateId());
        }

        RuleTemplateDOExample example1 = new RuleTemplateDOExample();
        example1.createCriteria().andIdIn(templateIdList).andIsDeleteEqualTo(0);
        long num = ruleTemplateDao.countByExample(example1);
        return num > 0;
    }

    /**
     * 返回默认的返回体.
     *
     * @param <E> 枚举类型
     * @return 默认返回数据
     */
    private <E> PageListDTO<E> defaultData() {
        PageListDTO<E> data = new PageListDTO<E>();
        data.setTotalSize(0);
        data.setList(new ArrayList<>());
        return data;
    }
}
