
package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.BusinessDOExample;
import com.ark.rule.platform.domain.dao.domain.BusinessDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusinessDao {
    long countByExample(BusinessDOExample example);

    int deleteByExample(BusinessDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BusinessDO record);

    int insertSelective(BusinessDO record);

    List<BusinessDO> selectByExample(BusinessDOExample example);

    BusinessDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BusinessDO record, @Param("example") BusinessDOExample example);

    int updateByExample(@Param("record") BusinessDO record, @Param("example") BusinessDOExample example);

    int updateByPrimaryKeySelective(BusinessDO record);

    int updateByPrimaryKey(BusinessDO record);
}