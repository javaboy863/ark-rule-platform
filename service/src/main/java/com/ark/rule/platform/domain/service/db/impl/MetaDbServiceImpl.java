
package com.ark.rule.platform.domain.service.db.impl;

import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.MetaDao;
import com.ark.rule.platform.domain.dao.domain.MetaDO;
import com.ark.rule.platform.domain.dao.domain.MetaDOExample;
import com.ark.rule.platform.domain.dto.response.MetaBaseDTO;
import com.ark.rule.platform.domain.service.db.IMetaDbService;
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
@Service("metaService")
@Slf4j
public class MetaDbServiceImpl implements IMetaDbService {
    @Resource
    private MetaDao metaDao;

    @Override
    public List<MetaBaseDTO> queryMetaByCodes(List<String> metaCodes) {
        if (CollectionUtils.isEmpty(metaCodes)) {
            return Lists.newArrayList();
        }
        MetaDOExample example = new MetaDOExample();
        example.createCriteria().andMetaCodeIn(metaCodes);
        List<MetaDO> metaDOList = metaDao.selectByExample(example);
        if (CollectionUtils.isEmpty(metaDOList)) {
            return Lists.newArrayList();
        }
        return metaDOList.stream()
                .map(metaDO -> BeanConvertUtil.conver(metaDO, MetaBaseDTO.class))
                .collect(Collectors.toList());
    }
}

