package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取所有分类列表
     */
    List<Category> listAll();
}
