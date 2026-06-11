package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Specification;
import com.demo.entity.SpecificationValue;

import java.util.List;

/**
 * 规格服务接口
 */
public interface SpecificationService extends IService<Specification> {

    /**
     * 根据分类ID查询规格列表
     */
    List<Specification> listByCategoryId(Long categoryId);

    /**
     * 根据规格ID查询规格值列表
     */
    List<SpecificationValue> listValuesBySpecId(Long specId);

    /**
     * 新增规格
     */
    void addSpecification(Specification specification);

    /**
     * 修改规格
     */
    void updateSpecification(Specification specification);

    /**
     * 删除规格
     */
    void deleteSpecification(Long id);

    /**
     * 新增规格值
     */
    void addSpecValue(SpecificationValue specValue);

    /**
     * 删除规格值
     */
    void deleteSpecValue(Long id);
}
