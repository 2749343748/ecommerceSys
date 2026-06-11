package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.dto.ProductDTO;
import com.demo.entity.Category;
import com.demo.entity.Product;
import com.demo.mapper.CategoryMapper;
import com.demo.mapper.ProductMapper;
import com.demo.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public IPage<Product> listPage(Integer pageNum, Integer pageSize, String keyword) {
        return listPage(pageNum, pageSize, keyword, null);
    }

    @Override
    public IPage<Product> listPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        if (categoryId != null) {
            // 获取该分类及其所有子分类的ID
            List<Long> categoryIds = getAllCategoryIds(categoryId);
            wrapper.in(Product::getCategoryId, categoryIds);
        }
        // 管理后台查询所有商品（不过滤状态）
        wrapper.orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(page, wrapper);
    }

    /**
     * 获取分类及其所有子分类的ID列表
     */
    private List<Long> getAllCategoryIds(Long categoryId) {
        List<Long> ids = new ArrayList<>();
        ids.add(categoryId);

        // 查询所有分类
        List<Category> allCategories = categoryMapper.selectList(null);
        // 递归获取子分类ID
        collectChildIds(categoryId, allCategories, ids);

        return ids;
    }

    /**
     * 递归收集子分类ID
     */
    private void collectChildIds(Long parentId, List<Category> allCategories, List<Long> ids) {
        for (Category category : allCategories) {
            if (parentId.equals(category.getParentId())) {
                ids.add(category.getId());
                collectChildIds(category.getId(), allCategories, ids);
            }
        }
    }

    @Override
    public void addProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        Product product = productMapper.selectById(productDTO.getId());
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        BeanUtils.copyProperties(productDTO, product);
        productMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }
}
