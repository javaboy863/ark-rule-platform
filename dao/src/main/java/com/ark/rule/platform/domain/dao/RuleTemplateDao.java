package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.RuleTemplateDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleTemplateDao {
    long countByExample(RuleTemplateDOExample example);

    int deleteByExample(RuleTemplateDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RuleTemplateDO record);

    int insertSelective(RuleTemplateDO record);

    List<RuleTemplateDO> selectByExample(RuleTemplateDOExample example);

    RuleTemplateDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RuleTemplateDO record, @Param("example") RuleTemplateDOExample example);

    int updateByExample(@Param("record") RuleTemplateDO record, @Param("example") RuleTemplateDOExample example);

    int updateByPrimaryKeySelective(RuleTemplateDO record);

    int updateByPrimaryKey(RuleTemplateDO record);
}
