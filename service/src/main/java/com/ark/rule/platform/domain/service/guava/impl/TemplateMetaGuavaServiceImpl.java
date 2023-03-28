package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.RuleTemplateMetaDao;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDOExample;
import com.ark.rule.platform.domain.dto.inner.IRuleTemplateMetaDTO;
import com.ark.rule.platform.domain.service.guava.ITemplateMetaGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 模板元数据guava实现.
 *
 */
@Service
@Slf4j
public class TemplateMetaGuavaServiceImpl implements ITemplateMetaGuavaService {

    @Resource
    private RuleTemplateMetaDao ruleTemplateMetaDao;

    /**
     * guava缓存cache
     */
    private LoadingCache<Long, Optional<List<IRuleTemplateMetaDTO>>> templateMetaCache;

    /**
     * 过期时间.
     */
    private static final int EXPIRE_TIME = 60 * 60;
    /**
     * 最大长度
     */
    private static final int MAX_SIZE = 100;

    /**
     * 初始化缓存.
     */
    @PostConstruct
    public void init() {
        this.templateMetaCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(AsyncCacheLoader.buildAsyncCacheLoader(this::queryTemplateMetaByTid,
                        AsFutureUtil.getExecutor()));
    }

    @Override
    public List<IRuleTemplateMetaDTO> getTemplateMetaByTid(Long templateId) {
        if (templateId == null) {
            return Lists.newArrayList();
        }

        try {
            Optional<List<IRuleTemplateMetaDTO>> templateMetaOptional = templateMetaCache.get(templateId);
            return templateMetaOptional.orElse(Lists.newArrayList());
        } catch (ExecutionException e) {
            log.error("getTemplateMetaByTid templateId:{} error:", templateId, e);
            return Lists.newArrayList();
        }
    }

    /**
     * 根据模板id查询模板对应元数据.
     *
     * @param templateId ''
     * @return ''
     */
    private Optional<List<IRuleTemplateMetaDTO>> queryTemplateMetaByTid(Long templateId) {
        RuleTemplateMetaDOExample example = new RuleTemplateMetaDOExample();
        example.createCriteria().andTemplateIdEqualTo(templateId);
        List<RuleTemplateMetaDO> templateMetas = null;
        try {
            templateMetas = ruleTemplateMetaDao.selectByExample(example);
        } catch (Exception e) {
            log.error("queryTemplateMetaByTid error templateId:{} e:", templateId, e);
        }
        if (CollectionUtils.isEmpty(templateMetas)) {
            return Optional.empty();
        }
        List<IRuleTemplateMetaDTO> ruleTemplateMetaDTOList = templateMetas.stream()
                .map(ruleTemplateMetaDO -> BeanConvertUtil
                    .conver(ruleTemplateMetaDO, IRuleTemplateMetaDTO.class))
                .collect(Collectors.toList());
        return Optional.of(ruleTemplateMetaDTOList);
    }
}

