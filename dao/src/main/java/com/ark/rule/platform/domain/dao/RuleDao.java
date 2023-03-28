package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.RuleDOExample;
import com.ark.rule.platform.domain.dao.domain.RuleDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleDao {
    long countByExample(RuleDOExample example);

    int deleteByExample(RuleDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RuleDO record);

    int insertSelective(RuleDO record);

    List<RuleDO> selectByExample(RuleDOExample example);

    RuleDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RuleDO record, @Param("example") RuleDOExample example);

    int updateByExample(@Param("record") RuleDO record, @Param("example") RuleDOExample example);

    int updateByPrimaryKeySelective(RuleDO record);

    int updateByPrimaryKey(RuleDO record);
}
