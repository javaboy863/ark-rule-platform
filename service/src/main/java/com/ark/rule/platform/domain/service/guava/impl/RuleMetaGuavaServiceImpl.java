
package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.RuleMetaDao;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDOExample;
import com.ark.rule.platform.domain.dto.inner.IRuleMetaDTO;
import com.ark.rule.platform.domain.service.config.RuleCommonConfig;
import com.ark.rule.platform.domain.service.guava.IRuleMetaGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述类的功能.
 *
 */
@Service("ruleMetaGuavaService")
@Slf4j
public class RuleMetaGuavaServiceImpl implements IRuleMetaGuavaService {

    @Resource
    private RuleMetaDao ruleMetaDao;

    /**
     * 规则查询规则元数据guava缓存.
     */
    private LoadingCache<Long, Optional<List<IRuleMetaDTO>>> ruleMetaCache;

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 30;
    /**
     * 最大组规则个数.
     */
    private static final int MAX_SIZE = 500;

    @Resource
    private RuleCommonConfig ruleCommonConfig;
    /**
     * b端直接查询db开关.
     */
    private static final int QUERY_DB = 1;
    /**
     * dubbo上下文参数 判断是否来自b端.
     */
    private static final String FROM_USER = "fromUser";
    /**
     * 1-c端 2-b端.
     */
    private static final String FROM_B = "2";

    /**
     * 初始化guava.
     */
    @PostConstruct
    public void init() {
        this.ruleMetaCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(
                    AsyncCacheLoader.buildAsyncCacheLoader(this::queryRuleMetaByRuleId, AsFutureUtil
                        .getExecutor()));
    }

    @Override
    public List<IRuleMetaDTO> getRuleMetaByRuleIds(List<Long> ruleIds) {
        if (CollectionUtils.isEmpty(ruleIds)) {
            return Lists.newArrayList();
        }
        // 通过RpcContext获取请求来源 来源b端直接查询db不走缓存.
        String fromUser = RpcContext.getContext().getAttachment(FROM_USER);
        if (QUERY_DB == ruleCommonConfig.getBgQueryDbSwitch() && FROM_B.equals(fromUser)) {
            log.info("getRuleMetaByRuleIds 来源b端,直接查询db");
            List<IRuleMetaDTO> ruleMetas = this.queryRuleMetaDB(ruleIds);
            setCache(ruleMetas);
            return ruleMetas;
        }

        List<IRuleMetaDTO> ruleMetaList = Lists.newArrayList();
        for (Long ruleId : ruleIds) {
            if (ruleId == null) {
                continue;
            }
            List<IRuleMetaDTO> ruleMetas = null;
            try {
                Optional<List<IRuleMetaDTO>> ruleMetaOptional = ruleMetaCache.get(ruleId);
                ruleMetas = ruleMetaOptional.orElse(Lists.newArrayList());
            } catch (ExecutionException e) {
                log.error("ruleId:{} getRuleMetaByRuleIds error:", ruleId, e);
            }
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ruleMetas)) {
                ruleMetaList.addAll(ruleMetas);
            }
        }
        return ruleMetaList;
    }

    /**
     * 刷入缓存.
     *
     * @param ruleMetas ''
     */
    private void setCache(List<IRuleMetaDTO> ruleMetas) {
        if (CollectionUtils.isEmpty(ruleMetas)) {
            return;
        }
        Map<Long, List<IRuleMetaDTO>> ruleMetaMap =
                ruleMetas.stream().collect(Collectors.groupingBy(IRuleMetaDTO::getRuleId));
        Map<Long, Optional<List<IRuleMetaDTO>>> cacheMap = Maps.newHashMap();
        for (Map.Entry<Long, List<IRuleMetaDTO>> entry : ruleMetaMap.entrySet()) {
            cacheMap.put(entry.getKey(), Optional.ofNullable(entry.getValue()));
        }
        ruleMetaCache.putAll(cacheMap);
    }

    private List<IRuleMetaDTO> queryRuleMetaDB(List<Long> ruleIds) {
        List<RuleMetaDO> ruleMetaList = null;
        try {
            RuleMetaDOExample ruleMetaExample = new RuleMetaDOExample();
            ruleMetaExample.createCriteria()
                    .andRuleIdIn(ruleIds);
            ruleMetaList = ruleMetaDao.selectByExample(ruleMetaExample);
        } catch (Exception e) {
            log.error("query ruleIds:{} meta error:", ruleIds, e);
        }
        if (CollectionUtils.isEmpty(ruleMetaList)) {
            return Lists.newArrayList();
        }
        return ruleMetaList.stream()
                .map(ruleMetaDO -> BeanConvertUtil.conver(ruleMetaDO, IRuleMetaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 查询rule下所有元数据值.
     *
     * @param ruleId ''
     * @return ''
     */
    private Optional<List<IRuleMetaDTO>> queryRuleMetaByRuleId(Long ruleId) {
        List<RuleMetaDO> ruleMetaList = null;
        try {
            RuleMetaDOExample ruleMetaExample = new RuleMetaDOExample();
            ruleMetaExample.createCriteria()
                    .andRuleIdEqualTo(ruleId);
            ruleMetaList = ruleMetaDao.selectByExample(ruleMetaExample);
        } catch (Exception e) {
            log.error("query ruleId:{} meta error:", ruleId, e);
        }
        if (CollectionUtils.isEmpty(ruleMetaList)) {
            return Optional.empty();
        }
        List<IRuleMetaDTO> ruleMetaDTOList = ruleMetaList.stream()
                .map(ruleMetaDO -> BeanConvertUtil.conver(ruleMetaDO, IRuleMetaDTO.class))
                .collect(Collectors.toList());
        return Optional.of(ruleMetaDTOList);
    }
}

