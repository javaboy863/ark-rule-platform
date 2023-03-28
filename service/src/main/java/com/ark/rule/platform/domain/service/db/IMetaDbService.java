package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.MetaBaseDTO;
import java.util.List;

/**
 * 查询db元数据信息.
 *
 */
public interface IMetaDbService {
    /**
     * 根据code查询元数据基本信息.
     *
     * @param metaCodes ''
     * @return ''
     */
    List<MetaBaseDTO> queryMetaByCodes(List<String> metaCodes);
}

