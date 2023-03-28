
package com.ark.rule.platform.domain.service.guava.impl;

import com.ark.rule.platform.common.util.AsFutureUtil;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.common.util.cache.AsyncCacheLoader;
import com.ark.rule.platform.domain.dao.MetaDao;
import com.ark.rule.platform.domain.dao.domain.MetaDO;
import com.ark.rule.platform.domain.dao.domain.MetaDOExample;
import com.ark.rule.platform.domain.dto.inner.IMetaDTO;
import com.ark.rule.platform.domain.service.guava.IMetaGuavaService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 元数据guava缓存.
 *
 */
@Slf4j
@Service("metaGuavaService")
public class MetaGuavaServiceImpl implements IMetaGuavaService {

    @Resource
    private MetaDao metaDao;

    private LoadingCache<String, Optional<IMetaDTO>> metaCache;

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 60 * 60;
    /**
     * 最大长度
     */
    private static final int MAX_SIZE = 200;

    /**
     * 初始化guava.
     */
    @PostConstruct
    public void init() {
        this.metaCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .maximumSize(MAX_SIZE)
                .build(AsyncCacheLoader.buildAsyncCacheLoader(this::queryMetaByCode, AsFutureUtil.getExecutor()));
    }

    @Override
    public IMetaDTO getMetaByMetaCode(String metaCode) {
        if (StringUtils.isBlank(metaCode)) {
            return null;
        }
        IMetaDTO metaDTO = null;
        try {
            Optional<IMetaDTO> metaOpt = metaCache.get(metaCode);
            metaDTO = metaOpt.orElse(null);
        } catch (ExecutionException e) {
            log.error("getMetaByMetaCode: metaCode = {}, ExecutionException = {}", metaCode, e);
        }
        return metaDTO;
    }

    @Override
    public List<IMetaDTO> getMetaByMetaCodeList(List<String> metaCodeList) {
        if (CollectionUtils.isEmpty(metaCodeList)) {
            return new ArrayList<>();
        }
        List<IMetaDTO> metaList = new ArrayList<>();
        List<String> needQuery = new ArrayList<>();
        for (String metaCode : metaCodeList) {
            IMetaDTO meta = null;
            try {
                Optional<IMetaDTO> metaOpt = metaCache.get(metaCode);
                meta = metaOpt.orElse(null);
            } catch (ExecutionException e) {
                log.error("metaCode:{} get meta error:", metaCode, e);
            }
            if (meta == null) {
                needQuery.add(metaCode);
            } else {
                metaList.add(meta);
            }
        }

        if (!needQuery.isEmpty()) {
            metaList.addAll(batchLoad(needQuery));
        }
        return metaList;
    }

    /**
     * 根据code查询元数据基本信息.
     *
     * @param metaCode code
     * @return ''
     */
    private Optional<IMetaDTO> queryMetaByCode(String metaCode) {
        MetaDOExample example = new MetaDOExample();
        example.createCriteria().andMetaCodeEqualTo(metaCode);
        List<MetaDO> metaList = metaDao.selectByExample(example);
        if (CollectionUtils.isEmpty(metaList)) {
            return Optional.empty();
        }
        IMetaDTO metaDTO = BeanConvertUtil.conver(metaList.get(0), IMetaDTO.class);
        return Optional.of(metaDTO);
    }

    private List<IMetaDTO> batchLoad(List<String> metaCodeList) {
        MetaDOExample example = new MetaDOExample();
        example.createCriteria().andMetaCodeIn(metaCodeList);
        List<MetaDO> queryMetas = metaDao.selectByExample(example);
        if (CollectionUtils.isEmpty(queryMetas)) {
            return new ArrayList<>();
        } else {
            List<IMetaDTO> metaDTOList = queryMetas.stream()
                    .map(metaDO -> BeanConvertUtil.conver(metaDO, IMetaDTO.class)).collect(Collectors.toList());
            for (IMetaDTO meta : metaDTOList) {
                metaCache.put(meta.getMetaCode(), Optional.of(meta));
            }
            return metaDTOList;
        }
    }
}
