package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.dto.ProductDTO;
import com.demo.entity.Product;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品
     */
    IPage<Product> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 分页查询商品（支持分类筛选）
     */
    IPage<Product> listPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId);

    /**
     * 新增商品
     */
    void addProduct(ProductDTO productDTO);

    /**
     * 修改商品
     */
    void updateProduct(ProductDTO productDTO);

    /**
     * 删除商品
     */
    void deleteProduct(Long id);

    /**
     * 修改商品状态（上架/下架）
     */
    void updateStatus(Long id, Integer status);
}
