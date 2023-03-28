

package com.ark.rule.platform.domain.service.guava;


import com.ark.rule.platform.domain.dto.inner.IGroupRuleDTO;
import java.util.List;

/**
 * 描述类的功能.
 *
 */
public interface IGroupGuavaService {
    /**
     * 根据分组id批量查询分组信息.
     *
     * @param idList id集合
     * @return 分组信息
     */
    List<IGroupRuleDTO> getGroupByIdList(List<Long> idList);

    /**
     * 根据id查询规则组信息.
     *
     * @param groupId ''
     * @return ''
     */
    IGroupRuleDTO getGroupById(Long groupId);
}
