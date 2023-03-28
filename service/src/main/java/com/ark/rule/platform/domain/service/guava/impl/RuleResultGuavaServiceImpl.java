
package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.RuleResultDao;
import com.ark.rule.platform.domain.dao.domain.RuleResultDO;
import com.ark.rule.platform.domain.dao.domain.RuleResultDOExample;
import com.ark.rule.platform.domain.dto.inner.IRuleResultDTO;
import com.ark.rule.platform.domain.service.guava.IRuleResultGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述类的功能.
 *
 */
@Service("ruleResultGuavaService")
@Slf4j
public class RuleResultGuavaServiceImpl implements IRuleResultGuavaService {
    @Resource
    private RuleResultDao ruleResultDao;

    /**
     * 规则查询规则元数据guava缓存.
     */
    private LoadingCache<Long, Optional<IRuleResultDTO>> ruleResultCache;

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 30;
    /**
     * 最大组规则个数.
     */
    private static final int MAX_SIZE = 200;

    /**
     * 初始化guava.
     */
    @PostConstruct
    public void init() {
        this.ruleResultCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(
                    AsyncCacheLoader.buildAsyncCacheLoader(this::queryResultByRuleId, AsFutureUtil.getExecutor()));
    }

    @Override
    public IRuleResultDTO getResultByRuleId(Long ruleId) {
        if (ruleId == null) {
            return null;
        }
        try {
            Optional<IRuleResultDTO> resultOptional = ruleResultCache.get(ruleId);
            return resultOptional.orElse(null);
        } catch (ExecutionException e) {
            log.error("ruleId:{} getResultByRuleId error:", ruleId, e);
            return null;
        }
    }

    /**
     * 查询rule返回结果.
     *
     * @param ruleId ''
     * @return ''
     */
    private Optional<IRuleResultDTO> queryResultByRuleId(Long ruleId) {
        RuleResultDOExample example = new RuleResultDOExample();
        example.createCriteria()
                .andRuleIdEqualTo(ruleId);
        List<RuleResultDO> ruleResults = ruleResultDao.selectByExample(example);
        if (CollectionUtils.isEmpty(ruleResults)) {
            return Optional.empty();
        }
        RuleResultDO ruleResultDO = ruleResults.get(0);
        IRuleResultDTO ruleResultDTO = BeanConvertUtil.conver(ruleResultDO, IRuleResultDTO.class);
        return Optional.of(ruleResultDTO);
    }
}

