
package com.ark.rule.platform.domain.dao;

import com.ark.rule.platform.domain.dao.domain.MetaDOExample;
import com.ark.rule.platform.domain.dao.domain.MetaDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MetaDao {
    long countByExample(MetaDOExample example);

    int deleteByExample(MetaDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MetaDO record);

    int insertSelective(MetaDO record);

    List<MetaDO> selectByExample(MetaDOExample example);

    MetaDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MetaDO record, @Param("example") MetaDOExample example);

    int updateByExample(@Param("record") MetaDO record, @Param("example") MetaDOExample example);

    int updateByPrimaryKeySelective(MetaDO record);

    int updateByPrimaryKey(MetaDO record);
}
