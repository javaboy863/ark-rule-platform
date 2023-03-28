package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.RuleResultDO;
import com.ark.rule.platform.domain.dao.domain.RuleResultDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleResultDao {
    long countByExample(RuleResultDOExample example);

    int deleteByExample(RuleResultDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RuleResultDO record);

    int insertSelective(RuleResultDO record);

    List<RuleResultDO> selectByExample(RuleResultDOExample example);

    RuleResultDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RuleResultDO record, @Param("example") RuleResultDOExample example);

    int updateByExample(@Param("record") RuleResultDO record, @Param("example") RuleResultDOExample example);

    int updateByPrimaryKeySelective(RuleResultDO record);

    int updateByPrimaryKey(RuleResultDO record);
}
