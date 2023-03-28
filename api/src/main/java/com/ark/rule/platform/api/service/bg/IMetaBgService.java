
package com.ark.rule.platform.api.service.bg;

import com.ark.rule.platform.api.domain.bg.QueryParamDTO;
import com.ark.rule.platform.api.domain.bg.MetaDTO;
import com.ark.rule.platform.api.domain.bg.PageListDTO;

/**
 * 业务线管理后台服务.
 *
 */
public interface IMetaBgService {

    /**
     * 保存实体.
     *
     * @param metaDTO 实体信息
     * @param user        用户名
     * @return 主键id
     */
    Long save(MetaDTO metaDTO, String user);

    /**
     * 删除.
     *
     * @param id   主键id
     * @param user 用户名
     * @return 主键id
     */
    Long delete(Long id, String user);

    /**
     * 分页从查询.
     *
     * @param query 查询参数
     * @return 返回列表
     */
    PageListDTO<MetaDTO> listByPage(QueryParamDTO query);

    /**
     * 根据id查询.
     *
     * @param id 主键id
     * @return 详细信息
     */
    MetaDTO getById(Long id);

    /**
     * 根据编码查询.
     *
     * @param code 编码
     * @return 详细信息
     */
    MetaDTO getByCode(String code);
}
