package com.ark.rule.platform.biz.bg;


import com.ark.rule.platform.api.domain.bg.BusinessDTO;
import com.ark.rule.platform.api.domain.bg.PageListDTO;
import com.ark.rule.platform.api.domain.bg.QueryParamDTO;
import com.ark.rule.platform.api.service.bg.IBusinessBgService;
import com.ark.rule.platform.domain.dao.BusinessDao;
import com.ark.rule.platform.domain.dao.domain.BusinessDO;
import com.ark.rule.platform.domain.dao.domain.BusinessDOExample;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 业务线管理服务.
 *
 */
@Slf4j
@Service("businessBgService")
public class BusinessBgServiceImpl implements IBusinessBgService {

    @Resource
    private BusinessDao businessDao;

    @Override
    public Long save(BusinessDTO businessDTO, String user) {
        checkSaveParam(businessDTO, user);
        Long id;
        if (businessDTO.getId() == null) {
            id = add(businessDTO, user);
        } else {
            id = update(businessDTO, user);
        }
        log.info("id = {}", id);
        return id;
    }

    private Long add(BusinessDTO businessDTO, String user) {
        BusinessDO saveDTO = new BusinessDO();
        saveDTO.setCreateUser(user);
        saveDTO.setUpdateUser(user);
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        saveDTO.setBizCode(businessDTO.getBizCode());
        saveDTO.setBizName(businessDTO.getBizName());
        saveDTO.setIsDelete(0);
        businessDao.insert(saveDTO);
        return businessDTO.getId();
    }

    private Long update(BusinessDTO businessDTO, String user) {
        BusinessDO saveDTO = businessDao.selectByPrimaryKey(businessDTO.getId());
        if (saveDTO == null) {
            throw new RuntimeException("data not exists, id = " + businessDTO.getId());
        }
        saveDTO.setBizCode(businessDTO.getBizCode());
        saveDTO.setBizName(businessDTO.getBizName());
        saveDTO.setUpdateTime(new Date());
        saveDTO.setUpdateUser(user);
        businessDao.updateByPrimaryKey(saveDTO);
        return saveDTO.getId();
    }

    private void checkSaveParam(BusinessDTO businessDTO, String user) {
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (businessDTO == null) {
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
        BusinessDO saveDTO = businessDao.selectByPrimaryKey(id);
        if (saveDTO == null || saveDTO.getIsDelete() == 1) {
            throw new RuntimeException("data not exists or delete, id = " + id);
        }
        saveDTO.setIsDelete(1);
        saveDTO.setUpdateUser(user);
        saveDTO.setUpdateTime(new Date());
        businessDao.updateByPrimaryKey(saveDTO);
        return id;
    }

    @Override
    public PageListDTO<BusinessDTO> listByPage(QueryParamDTO query) {
        query.check();
        BusinessDOExample example = editQueryExample(query);
        long num = businessDao.countByExample(example);
        log.info("num = {}", num);
        if (num <= 0) {
            return this.defaultData();
        }
        List<BusinessDO> businessDOS = businessDao.selectByExample(example);
        log.info("businessDOS = {}", businessDOS);
        if (CollectionUtils.isEmpty(businessDOS)) {
            return this.defaultData();
        }
        PageListDTO<BusinessDTO> result = editListResult(businessDOS, num);
        return result;
    }

    private PageListDTO<BusinessDTO> editListResult(List<BusinessDO> businessDOS, long num) {
        List<BusinessDTO> dataList = new ArrayList<>();
        PageListDTO<BusinessDTO> result = new PageListDTO<>();
        result.setTotalSize((int) num);
        result.setList(dataList);
        BusinessDTO temp;
        for (BusinessDO item : businessDOS) {
            temp = new BusinessDTO();
            BeanUtils.copyProperties(item, temp);
            dataList.add(temp);
        }
        return result;
    }

    private BusinessDOExample editQueryExample(QueryParamDTO query) {
        BusinessDOExample example = new BusinessDOExample();
        BusinessDOExample.Criteria criteria = example.createCriteria().andIsDeleteEqualTo(0);
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andBizNameLike("%" + query.getName() + "%");
        }
        if (StringUtils.isNotBlank(query.getCode())) {
            criteria.andBizCodeLike("%" + query.getCode() + "%");
        }
        example.setLimit(query.getSize());
        example.setOffset(query.getSize() * (query.getIndex() - 1));
        example.setOrderByClause("update_time desc");
        return example;
    }


    @Override
    public BusinessDTO getById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        BusinessDO businessDO = businessDao.selectByPrimaryKey(id);
        log.info("businessDO = {}", businessDO);
        if (businessDO == null) {
            return null;
        }
        BusinessDTO result = new BusinessDTO();
        BeanUtils.copyProperties(businessDO, result);
        return result;
    }

    @Override
    public BusinessDTO getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        BusinessDOExample example = new BusinessDOExample();
        example.createCriteria().andBizCodeEqualTo(code);
        List<BusinessDO> businessDOS = businessDao.selectByExample(example);
        log.info("businessDOS = {}", businessDOS);
        if (CollectionUtils.isEmpty(businessDOS)) {
            return null;
        }
        BusinessDTO result = new BusinessDTO();
        BeanUtils.copyProperties(businessDOS.get(0), result);
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
