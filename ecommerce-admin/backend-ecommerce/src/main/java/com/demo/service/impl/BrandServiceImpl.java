package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.dto.BrandDTO;
import com.demo.entity.Brand;
import com.demo.mapper.BrandMapper;
import com.demo.service.BrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 品牌服务实现类
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public IPage<Brand> listPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Brand> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Brand::getName, keyword);
        }
        wrapper.orderByAsc(Brand::getSort);
        return brandMapper.selectPage(page, wrapper);
    }

    @Override
    public void addBrand(BrandDTO brandDTO) {
        // 检查品牌名是否已存在
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Brand::getName, brandDTO.getName());
        if (brandMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("品牌名称已存在");
        }
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDTO, brand);
        brandMapper.insert(brand);
    }

    @Override
    public void updateBrand(BrandDTO brandDTO) {
        Brand brand = brandMapper.selectById(brandDTO.getId());
        if (brand == null) {
            throw new BusinessException("品牌不存在");
        }
        // 检查品牌名是否已存在
        if (StringUtils.hasText(brandDTO.getName()) && !brandDTO.getName().equals(brand.getName())) {
            LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Brand::getName, brandDTO.getName());
            if (brandMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("品牌名称已存在");
            }
        }
        BeanUtils.copyProperties(brandDTO, brand, "id");
        brandMapper.updateById(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        brandMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Brand brand = brandMapper.selectById(id);
        if (brand == null) {
            throw new BusinessException("品牌不存在");
        }
        brand.setStatus(status);
        brandMapper.updateById(brand);
    }
}
