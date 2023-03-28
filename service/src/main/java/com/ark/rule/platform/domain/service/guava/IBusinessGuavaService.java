

package com.ark.rule.platform.domain.service.guava;

import com.ark.rule.platform.domain.dto.inner.IBusinessDTO;

/**
 * 业务线guava缓存.
 *
 */
public interface IBusinessGuavaService {

    /**
     * 根据bizCode查询业务线信息.
     *
     * @param bizCode 业务线编码
     * @return 业务线信息
     */
    IBusinessDTO getBusinessByBizCode(String bizCode);
}
