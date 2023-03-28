
package com.ark.rule.platform.domain.service.guava;


import com.ark.rule.platform.domain.dto.inner.IRuleMetaDTO;
import java.util.List;

/**
 * 规则元数据查询guava缓存.
 *
 */
public interface IRuleMetaGuavaService {
    /**
     * 获取rule 下的所有meta.
     *
     * @param ruleIds 规则id列表
     * @return ''
     */
    List<IRuleMetaDTO> getRuleMetaByRuleIds(List<Long> ruleIds);
}

