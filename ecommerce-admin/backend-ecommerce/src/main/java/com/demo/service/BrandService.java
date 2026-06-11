package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.dto.BrandDTO;
import com.demo.entity.Brand;

/**
 * 品牌服务接口
 */
public interface BrandService extends IService<Brand> {

    /**
     * 分页查询品牌列表
     */
    IPage<Brand> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 新增品牌
     */
    void addBrand(BrandDTO brandDTO);

    /**
     * 修改品牌
     */
    void updateBrand(BrandDTO brandDTO);

    /**
     * 删除品牌
     */
    void deleteBrand(Long id);

    /**
     * 修改品牌状态
     */
    void updateStatus(Long id, Integer status);
}
