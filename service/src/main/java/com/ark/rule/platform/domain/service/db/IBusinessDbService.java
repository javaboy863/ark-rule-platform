package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.BusinessBaseResDTO;

/**
 * 查询db业务线信息.
 *
 */
public interface IBusinessDbService {
    /**
     * 查询业务线基本信息.
     *
     * @param bizCode 业务线code
     * @return ''
     */
    BusinessBaseResDTO queryBusinessByCode(String bizCode);
}

