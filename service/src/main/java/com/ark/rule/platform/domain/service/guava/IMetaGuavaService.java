
package com.ark.rule.platform.domain.service.guava;


import com.ark.rule.platform.domain.dto.inner.IMetaDTO;
import java.util.List;

/**
 * 元数据guava缓存.
 *
 */
public interface IMetaGuavaService {
    /**
     * 根据元数据编码查询元数据信息.
     *
     * @param metaCode 元数据编码
     * @return 元数据信息
     */
    IMetaDTO getMetaByMetaCode(String metaCode);

    /**
     * 根据元数据编码列表批量查询元数据信息.
     *
     * @param metaCodeList 元数据编码列表
     * @return 元数据信息列表
     */
    List<IMetaDTO> getMetaByMetaCodeList(List<String> metaCodeList);
}
