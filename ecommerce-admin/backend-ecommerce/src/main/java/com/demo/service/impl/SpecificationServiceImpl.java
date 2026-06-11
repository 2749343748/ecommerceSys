package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Specification;
import com.demo.entity.SpecificationValue;
import com.demo.mapper.SpecificationMapper;
import com.demo.mapper.SpecificationValueMapper;
import com.demo.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规格服务实现类
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationValueMapper specValueMapper;

    @Override
    public List<Specification> listByCategoryId(Long categoryId) {
        return specificationMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<SpecificationValue> listValuesBySpecId(Long specId) {
        return specValueMapper.selectBySpecId(specId);
    }

    @Override
    public void addSpecification(Specification specification) {
        specificationMapper.insert(specification);
    }

    @Override
    public void updateSpecification(Specification specification) {
        specificationMapper.updateById(specification);
    }

    @Override
    public void deleteSpecification(Long id) {
        specificationMapper.deleteById(id);
        // 删除规格值
        LambdaQueryWrapper<SpecificationValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpecificationValue::getSpecId, id);
        specValueMapper.delete(wrapper);
    }

    @Override
    public void addSpecValue(SpecificationValue specValue) {
        specValueMapper.insert(specValue);
    }

    @Override
    public void deleteSpecValue(Long id) {
        specValueMapper.deleteById(id);
    }
}
