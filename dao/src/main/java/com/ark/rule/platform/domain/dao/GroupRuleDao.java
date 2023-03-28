
package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.GroupRuleDO;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GroupRuleDao {
    long countByExample(GroupRuleDOExample example);

    int deleteByExample(GroupRuleDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GroupRuleDO record);

    int insertSelective(GroupRuleDO record);

    List<GroupRuleDO> selectByExample(GroupRuleDOExample example);

    GroupRuleDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GroupRuleDO record, @Param("example") GroupRuleDOExample example);

    int updateByExample(@Param("record") GroupRuleDO record, @Param("example") GroupRuleDOExample example);

    int updateByPrimaryKeySelective(GroupRuleDO record);

    int updateByPrimaryKey(GroupRuleDO record);
}
