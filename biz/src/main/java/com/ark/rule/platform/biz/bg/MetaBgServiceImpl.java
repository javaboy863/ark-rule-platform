package com.ark.rule.platform.biz.bg;

import com.ark.rule.platform.api.domain.bg.MetaDTO;
import com.ark.rule.platform.api.domain.bg.PageListDTO;
import com.ark.rule.platform.api.domain.bg.QueryParamDTO;
import com.ark.rule.platform.api.service.bg.IMetaBgService;
import com.ark.rule.platform.domain.dao.MetaDao;
import com.ark.rule.platform.domain.dao.domain.MetaDO;
import com.ark.rule.platform.domain.dao.domain.MetaDOExample;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 元数据管理服务.
 *
 */
@Slf4j
@Service("metaBgService")
public class MetaBgServiceImpl implements IMetaBgService {

    @Resource
    private MetaDao metaDao;

    @Override
    public Long save(MetaDTO metaDTO, String user) {
        checkSaveParam(metaDTO, user);
        Long id;
        if (metaDTO.getId() == null) {
            id = add(metaDTO, user);
        } else {
            id = update(metaDTO, user);
        }
        log.info("id = {}", id);
        return id;
    }

    private Long update(MetaDTO metaDTO, String user) {
        MetaDO metaDO = metaDao.selectByPrimaryKey(metaDTO.getId());
        log.info("metaDO = {}", metaDO);
        if (metaDO == null || metaDO.getIsDelete() == 1) {
            throw new IllegalArgumentException("data not exits or delete :" + metaDTO.getId());
        }
        metaDO.setMetaName(metaDTO.getMetaName());
        metaDO.setMetaCode(metaDTO.getMetaCode());
        metaDO.setLimitOperator(metaDTO.getLimitOperator());
        metaDO.setValueType(metaDTO.getValueType());
        metaDO.setDefaultValue(metaDTO.getDefaultValue());
        metaDO.setRemark(metaDTO.getRemark());
        metaDO.setUpdateUser(user);
        metaDO.setUpdateTime(new Date());
        metaDao.updateByPrimaryKey(metaDO);
        return metaDO.getId();
    }

    private Long add(MetaDTO metaDTO, String user) {
        MetaDO metaDO = new MetaDO();
        metaDO.setMetaName(metaDTO.getMetaName());
        metaDO.setMetaCode(metaDTO.getMetaCode());
        metaDO.setLimitOperator(metaDTO.getLimitOperator());
        metaDO.setValueType(metaDTO.getValueType());
        metaDO.setDefaultValue(metaDTO.getDefaultValue());
        metaDO.setRemark(metaDTO.getRemark());
        metaDO.setIsDelete(0);
        metaDO.setCreateUser(user);
        metaDO.setUpdateUser(user);
        metaDO.setCreateTime(new Date());
        metaDO.setUpdateTime(new Date());
        metaDao.insert(metaDO);
        return metaDO.getId();
    }

    private void checkSaveParam(MetaDTO metaDTO, String user) {
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (metaDTO == null) {
            throw new IllegalArgumentException("businessDTO can not be null");
        }
    }

    @Override
    public Long delete(Long id, String user) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id 非法:" + id);
        }
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("user can not be null");
        }
        MetaDO saveDTO = metaDao.selectByPrimaryKey(id);
        if (saveDTO == null || saveDTO.getIsDelete() == 1) {
            throw new RuntimeException("data not exists or delete, id = " + id);
        }
        saveDTO.setIsDelete(1);
        saveDTO.setUpdateUser(user);
        saveDTO.setUpdateTime(new Date());
        metaDao.updateByPrimaryKey(saveDTO);
        return id;
    }

    @Override
    public PageListDTO<MetaDTO> listByPage(QueryParamDTO query) {
        query.check();
        MetaDOExample example = editQueryExample(query);
        long num = metaDao.countByExample(example);
        log.info("num = {}", num);
        if (num <= 0) {
            return this.defaultData();
        }
        List<MetaDO> businessDOS = metaDao.selectByExample(example);
        log.info("businessDOS = {}", businessDOS);
        if (CollectionUtils.isEmpty(businessDOS)) {
            return this.defaultData();
        }
        PageListDTO<MetaDTO> result = editListResult(businessDOS, num);
        return result;
    }

    private PageListDTO<MetaDTO> editListResult(List<MetaDO> businessDOS, long num) {
        List<MetaDTO> dataList = new ArrayList<>();
        PageListDTO<MetaDTO> result = new PageListDTO<>();
        result.setTotalSize((int) num);
        result.setList(dataList);
        MetaDTO temp;
        for (MetaDO item : businessDOS) {
            temp = new MetaDTO();
            BeanUtils.copyProperties(item, temp);
            dataList.add(temp);
        }
        return result;
    }

    private MetaDOExample editQueryExample(QueryParamDTO query) {
        MetaDOExample example = new MetaDOExample();
        MetaDOExample.Criteria criteria = example.createCriteria().andIsDeleteEqualTo(0);
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andMetaNameLike("%" + query.getName() + "%");
        }
        if (StringUtils.isNotBlank(query.getCode())) {
            criteria.andMetaCodeLike("%" + query.getCode() + "%");
        }
        example.setLimit(query.getSize());
        example.setOffset(query.getSize() * (query.getIndex() - 1));
        example.setOrderByClause("update_time desc");
        return example;
    }

    @Override
    public MetaDTO getById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        MetaDO metaDO = metaDao.selectByPrimaryKey(id);
        log.info("metaDO = {}", metaDO);
        if (metaDO == null) {
            return null;
        }
        MetaDTO result = new MetaDTO();
        BeanUtils.copyProperties(metaDO, result);
        return result;
    }

    @Override
    public MetaDTO getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        MetaDOExample example = new MetaDOExample();
        example.createCriteria().andMetaCodeEqualTo(code);
        List<MetaDO> metaDOS = metaDao.selectByExample(example);
        log.info("metaDOS = {}", metaDOS);
        if (CollectionUtils.isEmpty(metaDOS)) {
            return null;
        }
        MetaDTO result = new MetaDTO();
        BeanUtils.copyProperties(metaDOS.get(0), result);
        return result;
    }

    /**
     * 返回默认的返回体.
     *
     * @param <E> 枚举类型
     * @return 默认返回数据
     */
    private <E> PageListDTO<E> defaultData() {
        PageListDTO<E> data = new PageListDTO<E>();
        data.setTotalSize(0);
        data.setList(new ArrayList<>());
        return data;
    }
}
