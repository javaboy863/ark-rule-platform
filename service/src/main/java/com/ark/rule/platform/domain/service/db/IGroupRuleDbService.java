package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.GroupRuleBaseResDTO;
import java.util.List;

/**
 * 查询db规则组信息.
 *
 */
public interface IGroupRuleDbService {

    /**
     * 查询规则组基本信息.
     *
     * @param groupId 规则组id
     * @return ''
     */
    GroupRuleBaseResDTO queryGroupRuleBaseInfo(Long groupId);

    /**
     * 获取所有有效的规则组.
     *
     * @return ''
     */
    List<GroupRuleBaseResDTO> queryAllValidGroup();
}

