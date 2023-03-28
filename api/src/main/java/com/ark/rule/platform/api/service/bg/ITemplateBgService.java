
package com.ark.rule.platform.api.service.bg;

import com.ark.rule.platform.api.domain.bg.QueryParamDTO;
import com.ark.rule.platform.api.domain.bg.PageListDTO;
import com.ark.rule.platform.api.domain.bg.RuleTemplateDTO;

/**
 * 模板相关服务.
 *
 */
public interface ITemplateBgService {

    /**
     * 当前业务线下是否存在模板.
     *
     * @param bizCode 业务线编码
     * @return 是否存在
     */
    boolean isUsed(String bizCode);

    /**
     * 添加规则模板.
     *
     * @param dto  模板实体
     * @param user 用户名
     * @return id
     */
    Long add(RuleTemplateDTO dto, String user);

    /**
     * 删除规则模板.
     *
     * @param id   主键id
     * @param user 用户名
     * @return id
     */
    Long delete(Long id, String user);

    /**
     * 分页查询.
     *
     * @param query 查询条件
     * @return 查询结果
     */
    PageListDTO<RuleTemplateDTO> listByPage(QueryParamDTO query);

    /**
     * 根据id查询详细信息.
     *
     * @param id id
     * @return 详细信息
     */
    RuleTemplateDTO getById(Long id);

    /**
     * 是否使用了该metaCode.
     *
     * @param metaCode 元数据编码
     * @return 是/否
     */
    boolean isUserMeta(String metaCode);
}
