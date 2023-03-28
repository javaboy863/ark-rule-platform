package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.RuleMetaDOExample;
import com.ark.rule.platform.domain.dao.domain.RuleMetaDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleMetaDao {
    long countByExample(RuleMetaDOExample example);

    int deleteByExample(RuleMetaDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RuleMetaDO record);

    int insertSelective(RuleMetaDO record);

    List<RuleMetaDO> selectByExample(RuleMetaDOExample example);

    RuleMetaDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RuleMetaDO record, @Param("example") RuleMetaDOExample example);

    int updateByExample(@Param("record") RuleMetaDO record, @Param("example") RuleMetaDOExample example);

    int updateByPrimaryKeySelective(RuleMetaDO record);

    int updateByPrimaryKey(RuleMetaDO record);

    int batchInsert(@Param("ruleMetaDOList") List<RuleMetaDO> ruleMetaDOList);
}
