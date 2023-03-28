

package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.BusinessDao;
import com.ark.rule.platform.domain.dao.domain.BusinessDO;
import com.ark.rule.platform.domain.dao.domain.BusinessDOExample;
import com.ark.rule.platform.domain.dto.inner.IBusinessDTO;
import com.ark.rule.platform.domain.service.guava.IBusinessGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 业务线guava缓存.
 *
 */
@Service("businessGuavaService")
@Slf4j
public class BusinessGuavaServiceImpl implements IBusinessGuavaService {

    @Resource
    private BusinessDao businessDao;

    /**
     * guava缓存cache
     */
    private LoadingCache<String, Optional<IBusinessDTO>> businessCache;

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
        this.businessCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(AsyncCacheLoader.buildAsyncCacheLoader(this::getBusinessByCode, AsFutureUtil.getExecutor()));
    }

    @Override
    public IBusinessDTO getBusinessByBizCode(String bizCode) {
        if (StringUtils.isBlank(bizCode)) {
            return null;
        }
        IBusinessDTO businessDTO = null;
        try {
            Optional<IBusinessDTO> businessOpt = businessCache.get(bizCode);
            businessDTO = businessOpt.orElse(null);
        } catch (ExecutionException e) {
            log.error("getBusinessByBizCode: bizCode = {}, exception = {}", bizCode, e);
        }
        return businessDTO;
    }

    /**
     * 根据业务code查询业务信息.
     *
     * @param bizCode ''
     * @return ''
     */
    private Optional<IBusinessDTO> getBusinessByCode(String bizCode) {
        BusinessDOExample example = new BusinessDOExample();
        example.createCriteria().andBizCodeEqualTo(bizCode);
        List<BusinessDO> businessList = businessDao.selectByExample(example);
        if (CollectionUtils.isEmpty(businessList)) {
            return Optional.empty();
        }
        IBusinessDTO businessDTO = BeanConvertUtil.conver(businessList.get(0), IBusinessDTO.class);
        return Optional.of(businessDTO);
    }
}
