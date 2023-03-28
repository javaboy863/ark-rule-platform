
package com.ark.rule.platform.domain.service.guava.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.RuleDao;
import com.ark.rule.platform.domain.dao.domain.RuleDO;
import com.ark.rule.platform.domain.dao.domain.RuleDOExample;
import com.ark.rule.platform.domain.dto.inner.IRuleDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleMetaDTO;
import com.ark.rule.platform.domain.enums.DataValueEnum;
import com.ark.rule.platform.domain.service.config.RuleCommonConfig;
import com.ark.rule.platform.domain.service.guava.IRuleGuavaService;
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
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则查询guava缓存.
 *
 */
@Service("ruleGuavaService")
@Slf4j
public class RuleGuavaServiceImpl implements IRuleGuavaService {
    @Resource
    private RuleDao ruleDao;

    @Resource
    private IRuleMetaGuavaService ruleMetaGuavaService;

    @Resource
    private RuleCommonConfig ruleCommonConfig;

    /**
     * 规则查询规则guava缓存.
     */
    private LoadingCache<Long, Optional<List<IRuleDTO>>> groupRuleCache;
    /**
     * 规则组元数据值对应规则列表对应规则缓存.
     * key:元数据code value: key: 元数据值 v:规则id.
     */
    private LoadingCache<Long, Map<String, Map<String, List<Long>>>> groupMetaValueCache;

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 30;
    /**
     * 最大组规则个数.
     */
    private static final int MAX_SIZE = 300;
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
        this.groupRuleCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(
                    AsyncCacheLoader.buildAsyncCacheLoader(this::queryRulesByGroupId, AsFutureUtil.getExecutor()));
        this.groupMetaValueCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(AsyncCacheLoader.buildAsyncCacheLoader(this::queryMetaValueRules, AsFutureUtil.getExecutor()));
    }

    @Override
    public List<IRuleDTO> getGroupRuleByGroupId(Long groupId) {
        if (groupId == null) {
            return Lists.newArrayList();
        }
        // 通过RpcContext获取请求来源 来源b端直接查询db不走缓存.
        String fromUser = RpcContext.getContext().getAttachment(FROM_USER);
        if (QUERY_DB == ruleCommonConfig.getBgQueryDbSwitch() && FROM_B.equals(fromUser)) {
            log.info("getGroupRuleByGroupId 来源b端,直接查询db");
            Optional<List<IRuleDTO>> rulesOptional = queryRulesByGroupId(groupId);
            //刷入缓存
            groupRuleCache.put(groupId, rulesOptional);
            return rulesOptional.orElse(Lists.newArrayList());
        }
        List<IRuleDTO> rules = null;
        try {
            Optional<List<IRuleDTO>> ruleOptional = groupRuleCache.get(groupId);
            rules = ruleOptional.orElse(Lists.newArrayList());
        } catch (ExecutionException e) {
            log.error("groupId:{} getGroupRuleByGroupId error: ", groupId, e);
        }
        return rules;
    }

    @Override
    public Map<String, Map<String, List<Long>>> getMetaValueRuleIds(Long groupId) {
        // 通过RpcContext获取请求来源 来源b端直接查询db不走缓存.
        String fromUser = RpcContext.getContext().getAttachment(FROM_USER);
        if (QUERY_DB == ruleCommonConfig.getBgQueryDbSwitch() && FROM_B.equals(fromUser)) {
            log.info("getMetaValueRuleIds 来源b端,直接查询db");
            Map<String, Map<String, List<Long>>> metaValueRulesMap = queryMetaValueRules(groupId);
            groupMetaValueCache.put(groupId, metaValueRulesMap);
            return metaValueRulesMap;
        }
        Map<String, Map<String, List<Long>>> valueRuleMap = null;
        try {
            valueRuleMap = groupMetaValueCache.get(groupId);
        } catch (ExecutionException e) {
            log.error("groupId:{} getMetaValueRuleIds error: ", groupId, e);
        }
        return valueRuleMap;
    }

    /**
     * 查询group下所有规则信息.
     *
     * @param groupId 组id
     * @return ''
     */
    private Optional<List<IRuleDTO>> queryRulesByGroupId(Long groupId) {
        RuleDOExample example = new RuleDOExample();
        example.createCriteria()
                .andGroupIdEqualTo(groupId)
                .andIsDeleteEqualTo(DataValueEnum.NO_DELETA.getCode());
        List<RuleDO> ruleList = null;
        try {
            ruleList = ruleDao.selectByExample(example);
            log.debug("groupId:{} 查询到rule列表:{}", groupId, JSON.toJSONString(ruleList));
        } catch (Exception e) {
            log.error("query groupId:{} meta error:", groupId, e);
        }
        if (CollectionUtils.isEmpty(ruleList)) {
            return Optional.empty();
        }
        List<IRuleDTO> rules = ruleList.stream()
                .map(rule -> BeanConvertUtil.conver(rule, IRuleDTO.class))
                .collect(Collectors.toList());
        return Optional.of(rules);
    }

    /**
     * 查询规则组下元数据值对应的规则列表.
     *
     * @param groupId 组id
     * @return key:元数据code value: key: 元数据值 v:规则id
     */
    private Map<String, Map<String, List<Long>>> queryMetaValueRules(Long groupId) {
        List<IRuleDTO> ruleList = this.getGroupRuleByGroupId(groupId);
        if (CollectionUtils.isEmpty(ruleList)) {
            return Maps.newHashMap();
        }
        List<Long> ruleIds = ruleList.stream().map(IRuleDTO::getId).collect(Collectors.toList());
        List<IRuleMetaDTO> ruleMetas = ruleMetaGuavaService.getRuleMetaByRuleIds(ruleIds);
        if (CollectionUtils.isEmpty(ruleMetas)) {
            return Maps.newHashMap();
        }
        Map<String, Map<String, List<Long>>> metaValueRuleMap = Maps.newHashMap();
        for (IRuleMetaDTO ruleMetaDTO : ruleMetas) {
            Map<String, List<Long>> valueRuleMap = metaValueRuleMap.get(ruleMetaDTO.getMetaCode());
            if (MapUtils.isEmpty(valueRuleMap)) {
                valueRuleMap = Maps.newHashMap();
                metaValueRuleMap.put(ruleMetaDTO.getMetaCode(), valueRuleMap);
            }
            List<Long> ruleIdList = valueRuleMap.get(ruleMetaDTO.getMetaValue());
            if (ruleIdList == null) {
                ruleIdList = Lists.newArrayList();
                valueRuleMap.put(ruleMetaDTO.getMetaValue(), ruleIdList);
            }
            ruleIdList.add(ruleMetaDTO.getRuleId());
        }
        return metaValueRuleMap;
    }
}

