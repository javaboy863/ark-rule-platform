

package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.GroupRuleDao;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDO;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDOExample;
import com.ark.rule.platform.domain.dto.inner.IGroupRuleDTO;
import com.ark.rule.platform.domain.service.guava.IGroupGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
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
 * 规则组查询guava缓存.
 *
 */
@Service("groupGuavaService")
@Slf4j
public class GroupGuavaServiceImpl implements IGroupGuavaService {

    @Resource
    private GroupRuleDao groupRuleDao;

    private LoadingCache<Long, Optional<IGroupRuleDTO>> groupCache;

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 30;
    /**
     * 最大长度
     */
    private static final int MAX_SIZE = 100;

    /**
     * 初始化guava.
     */
    @PostConstruct
    public void init() {
        this.groupCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(AsyncCacheLoader.buildAsyncCacheLoader(this::queryGroupById, AsFutureUtil.getExecutor()));
    }

    @Override
    public List<IGroupRuleDTO> getGroupByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        List<IGroupRuleDTO> groupList = Lists.newArrayList();
        for (Long groupId : idList) {
            if (groupId == null) {
                continue;
            }
            try {
                Optional<IGroupRuleDTO> groupOptional = groupCache.get(groupId);
                groupOptional.ifPresent(groupList::add);
            } catch (ExecutionException e) {
                log.error("groupId:{} query group info error:", groupId, e);
            }

        }
        return groupList;
    }

    @Override
    public IGroupRuleDTO getGroupById(Long groupId) {
        try {
            Optional<IGroupRuleDTO> groupOptional = groupCache.get(groupId);
            return groupOptional.orElse(null);
        } catch (ExecutionException e) {
            log.error("groupId:{} query group info error:", groupId, e);
            return null;
        }
    }

    /**
     * 根据id查询规则组信息.
     *
     * @param groupId ''
     * @return ''
     */
    private Optional<IGroupRuleDTO> queryGroupById(Long groupId) {
        GroupRuleDOExample example = new GroupRuleDOExample();
        example.createCriteria()
                .andIdEqualTo(groupId)
                .andIsDeleteEqualTo(0);
        List<GroupRuleDO> groupRules = groupRuleDao.selectByExample(example);
        if (CollectionUtils.isEmpty(groupRules)) {
            return Optional.empty();
        }
        GroupRuleDO groupRuleDO = groupRules.get(0);
        IGroupRuleDTO groupRuleDTO = BeanConvertUtil.conver(groupRuleDO, IGroupRuleDTO.class);
        return Optional.of(groupRuleDTO);
    }
}
