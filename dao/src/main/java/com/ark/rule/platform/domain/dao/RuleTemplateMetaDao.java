package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDO;
import com.ark.rule.platform.domain.dao.domain.RuleTemplateMetaDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleTemplateMetaDao {
    long countByExample(RuleTemplateMetaDOExample example);

    int deleteByExample(RuleTemplateMetaDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RuleTemplateMetaDO record);

    int insertSelective(RuleTemplateMetaDO record);

    List<RuleTemplateMetaDO> selectByExample(RuleTemplateMetaDOExample example);

    RuleTemplateMetaDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RuleTemplateMetaDO record, @Param("example") RuleTemplateMetaDOExample example);

    int updateByExample(@Param("record") RuleTemplateMetaDO record, @Param("example") RuleTemplateMetaDOExample example);

    int updateByPrimaryKeySelective(RuleTemplateMetaDO record);

    int updateByPrimaryKey(RuleTemplateMetaDO record);
}
