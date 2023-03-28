

package com.ark.rule.platform.domain.service.guava;


import com.ark.rule.platform.domain.dto.inner.IRuleDTO;
import java.util.List;
import java.util.Map;

/**
 * 描述类的功能.
 *
 */
public interface IRuleGuavaService {
    /**
     * 获取规则组下的规则.
     *
     * @param groupId 规则组id
     * @return ''
     */
    List<IRuleDTO> getGroupRuleByGroupId(Long groupId);

    /**
     * 根据规则组id查询规则组下所有规则元数据值对应的规则列表.
     *
     * @param groupId ''
     * @return key:元数据code value: key: 元数据值 v:规则id.
     */
    Map<String, Map<String, List<Long>>> getMetaValueRuleIds(Long groupId);
}
